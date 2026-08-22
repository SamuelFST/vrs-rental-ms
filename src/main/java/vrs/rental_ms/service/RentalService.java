package vrs.rental_ms.service;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vrs.rental_ms.document.Rental;
import vrs.rental_ms.dto.payment.PaymentResponseDTO;
import vrs.rental_ms.dto.rental.*;
import vrs.rental_ms.dto.user.UserResponseDTO;
import vrs.rental_ms.dto.vehicle.VehicleResponseDTO;
import vrs.rental_ms.enums.RentalStatus;
import vrs.rental_ms.enums.VehicleStatus;
import vrs.rental_ms.exception.BadRequestException;
import vrs.rental_ms.exception.NotFoundException;
import vrs.rental_ms.mapper.RentalMapper;
import vrs.rental_ms.queue.FileProducer;
import vrs.rental_ms.queue.RentalProducer;
import vrs.rental_ms.repository.RentalRepository;
import vrs.rental_ms.util.UserSecurityUtil;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static vrs.rental_ms.constants.Constants.BIG_DECIMAL_ZERO;
import static vrs.rental_ms.constants.Constants.PAYMENT_FACTOR;
import static vrs.rental_ms.enums.ErrorMessages.*;
import static vrs.rental_ms.enums.RentalStatus.CLOSED;
import static vrs.rental_ms.enums.VehicleStatus.AVAILABLE;

@Slf4j
@Service
@AllArgsConstructor
public class RentalService {

    private final SecurityService securityService;
    private final VehicleService vehicleService;
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final RentalProducer rentalProducer;
    private final PaymentService paymentService;
    private final MongoTemplate mongoTemplate;
    private final FileProducer fileProducer;

    public Page<RentalResponseDTO> findAllRentals(final RentalFilterDTO rentalFilterDTO,
                                                  Pageable pageable) {
        var query = this.buildRentalsQuery(rentalFilterDTO, pageable);
        var rentals = mongoTemplate.find(query, Rental.class);
        var total = mongoTemplate.count(query.skip(0).limit(0), Rental.class);

        return new PageImpl<>(rentalMapper.toRentalResponseDTOList(rentals), pageable, total);
    }

    public RentalResponseDTO findRentalById(final String rentalId) {
        return rentalMapper.toRentalResponseDTO(this.findRentalDocumentById(rentalId));
    }

    public RentalPriceResponse calculateRentalPrice(final Long vehicleId, final Long startDate, final Long endDate) {
        return new RentalPriceResponse(getFinalPrice(getRentalVehicle(vehicleId).getPriceBrl(), startDate, endDate));
    }

    public RentalResponseDTO createRental(final RentalRequestDTO rentalRequestDTO) {
        var vehicle = getRentalVehicle(rentalRequestDTO.getVehicleId());
        var user = getRentalUser(rentalRequestDTO.getUserId());
        var finalPrice = getFinalPrice(vehicle.getPriceBrl(), rentalRequestDTO.getStartDate(), rentalRequestDTO.getEndDate());

        var rental = rentalRepository.save(
                rentalMapper.toCreateRentalDocument(rentalRequestDTO, vehicle, user, finalPrice));

        rentalProducer.sendToProcessRentalQueue(rentalMapper.toRentalMessageDTO(rental, finalPrice, rentalRequestDTO, user));

        return rentalMapper.toRentalResponseDTO(rental);
    }

    public Rental findRentalDocumentById(final String id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RENTAL_NOT_FOUND.getMessage()));
    }

    public RentalResponseDTO finishRental(final String id, final RentalFinishRequestDTO rentalFinishRequestDTO) {
        var rental = Optional.of(this.findRentalDocumentById(id))
                .filter(rentalDocument -> rentalDocument.getStatus() != CLOSED)
                .map(rentalDocument -> rentalRepository.save(rentalDocument
                        .setStatus(RentalStatus.PROCESSING_CLOSING)
                        .setUpdatedAt(Instant.now())))
                .orElseThrow(() -> new BadRequestException(RENTAL_ALREADY_CLOSED.getMessage()));

        rentalProducer.sendToFinishRentalQueue(rentalMapper.toRentalFinishMessageDTO(id, rentalFinishRequestDTO));

        return rentalMapper.toRentalResponseDTO(rental);
    }

    public void processRental(final RentalMessageDTO rentalMessageDTO) {
        var userAddress = securityService.findUserAddressById(rentalMessageDTO.getUser().getId(), rentalMessageDTO.getAddressId());
        var paymentResponseDTO = paymentService.processPayment(rentalMessageDTO, userAddress);

        var rentalStatus = this.updateRentalStatusFromPayment(rentalMessageDTO.getRentalId(), paymentResponseDTO, null, null, null);
        this.updateVehicleStatus(rentalMessageDTO.getVehicleId(), rentalStatus, null);
    }

    public void processRentalFinish(final RentalFinishMessageDTO rentalFinishMessageDTO) {
        var rental = this.findRentalDocumentById(rentalFinishMessageDTO.getRentalId());
        var returnedValue = this.getReturnedValue(rental.getEndDate(), rentalFinishMessageDTO.getReceivedBackDate(), rental.getFinalPrice());

        var rentalStatus = this.updateRentalStatusFromPayment(rental.getId(),
                returnedValue.compareTo(BIG_DECIMAL_ZERO) > 0 ?
                        paymentService.refundPayment(returnedValue, rental.getPaymentTransactionId()) : null,
                rentalFinishMessageDTO.getRentalKmDriven(),
                returnedValue,
                rentalFinishMessageDTO.getReceivedBackDate());

        this.updateVehicleStatus(rental.getVehicleData().getId(), rentalStatus, rentalFinishMessageDTO.getRentalKmDriven());
        fileProducer.sendToGenerateRentalFile(new RentalFileMessageDTO().setRentalId(rental.getId()));
    }

    public void updateRentalWithError(final String rentalId) {
        rentalRepository.save(this.findRentalDocumentById(rentalId)
                .setStatus(RentalStatus.ERROR)
                .setUpdatedAt(Instant.now()));
    }

    public void updateRentalWithGeneratedContract(final String rentalId, final String filename) {
        rentalRepository.save(this.findRentalDocumentById(rentalId).setGeneratedContract(filename));
    }

    private RentalStatus updateRentalStatusFromPayment(final String rentalId,
                                                       @Nullable final PaymentResponseDTO paymentResponseDTO,
                                                       final BigDecimal rentalKmDriven,
                                                       final BigDecimal returnedValue,
                                                       final Long receivedBackDate) {
        var rental = this.findRentalDocumentById(rentalId);

        var rentalStatus = Optional.ofNullable(paymentResponseDTO)
                .map(PaymentResponseDTO::getStatus)
                .map(RentalStatus::fromPaymentStatus)
                .orElse(CLOSED);

        rental
                .setStatus(rentalStatus)
                .setUpdatedAt(Instant.now());

        Optional.ofNullable(paymentResponseDTO).ifPresent(paymentResponse -> {
            rental.setPaymentStatus(paymentResponse.getStatus());
            rental.setPaymentTransactionId(paymentResponse.getTransactionId());
        });
        Optional.ofNullable(rentalKmDriven).ifPresent(rental::setRentalKmDriven);
        Optional.ofNullable(returnedValue).ifPresent(rental::setReturnedValue);
        Optional.ofNullable(receivedBackDate).ifPresent(rental::setReceivedBackDate);

        rentalRepository.save(rental);

        return rentalStatus;
    }

    private void updateVehicleStatus(final Long vehicleId,
                                     final RentalStatus rentalStatus,
                                     final BigDecimal rentalKmDriven) {
        Optional.ofNullable(rentalKmDriven)
                        .ifPresentOrElse(
                                (kmDriven) -> vehicleService.updateVehicleRentalData(vehicleId, VehicleStatus.fromRentalStatus(rentalStatus), kmDriven),
                                () -> vehicleService.updateVehicleStatus(vehicleId, VehicleStatus.fromRentalStatus(rentalStatus)));
    }

    private VehicleResponseDTO getRentalVehicle(final Long vehicleId) {
        return Optional.of(vehicleService.findVehicleById(vehicleId))
                .filter(vehicleResponseDTO -> vehicleResponseDTO.getStatus() == AVAILABLE)
                .orElseThrow(() -> new BadRequestException(VEHICLE_NOT_AVAILABLE.getMessage()));
    }

    private UserResponseDTO getRentalUser(final Long userId) {
        var userResponseDTO = securityService.findUserById(userId);
        UserSecurityUtil.currentUserIsTheResourceOwner(userResponseDTO.getEmail());

        return userResponseDTO;
    }

    private BigDecimal getFinalPrice(final BigDecimal originalPrice, final Long rentalStartDate, final Long rentalEndDate) {
        var daysCount = getDaysBetweenDates(rentalStartDate, rentalEndDate) + 1;
        return originalPrice.multiply(PAYMENT_FACTOR).multiply(new BigDecimal(daysCount));
    }

    private BigDecimal getReturnedValue(final Long rentalEndDate,
                                        final Long receivedBackDate,
                                        final BigDecimal finalPrice) {
        return paymentService.calculateValueToBeReturned(getDaysBetweenDates(rentalEndDate, receivedBackDate), finalPrice);
    }

    private Long getDaysBetweenDates(final Long startDate, final Long endDate) {
        var firstDate = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate();
        var secondDate = Instant.ofEpochMilli(endDate).atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(firstDate, secondDate);
    }

    private Query buildRentalsQuery(final RentalFilterDTO rentalFilterDTO, final Pageable pageable) {
        var query = new Query();
        List<Criteria> criterias = new ArrayList<>();

        var emailFilter = Boolean.TRUE.equals(UserSecurityUtil.currentUserIsAdmin()) ?
                rentalFilterDTO.getEmail() : UserSecurityUtil.getCurrentUserEmail();

        Optional.ofNullable(emailFilter)
                .ifPresent(email -> criterias.add(Criteria.where("userData.email").regex(emailFilter, "i")));

        Optional.ofNullable(rentalFilterDTO.getStatus())
                .ifPresent(status -> criterias.add(Criteria.where("status").is(rentalFilterDTO.getStatus())));

        Optional.ofNullable(rentalFilterDTO.getLicensePlate())
                .ifPresent(licensePlate -> criterias.add(Criteria.where("vehicleData.licensePlate").regex(rentalFilterDTO.getLicensePlate(), "i")));


        if (Boolean.FALSE.equals(criterias.isEmpty())) {
            return query
                    .addCriteria(new Criteria().andOperator(criterias.toArray(new Criteria[0])))
                    .with(pageable);
        }

        return query.with(pageable);
    }

}
