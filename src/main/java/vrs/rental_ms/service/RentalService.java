package vrs.rental_ms.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vrs.rental_ms.dto.payment.PaymentResponseDTO;
import vrs.rental_ms.dto.rental.RentalMessageDTO;
import vrs.rental_ms.dto.rental.RentalRequestDTO;
import vrs.rental_ms.dto.rental.RentalResponseDTO;
import vrs.rental_ms.dto.user.UserResponseDTO;
import vrs.rental_ms.dto.vehicle.VehicleResponseDTO;
import vrs.rental_ms.enums.RentalStatus;
import vrs.rental_ms.enums.VehicleStatus;
import vrs.rental_ms.exception.BadRequestException;
import vrs.rental_ms.exception.NotFoundException;
import vrs.rental_ms.mapper.RentalMapper;
import vrs.rental_ms.queue.RentalProducer;
import vrs.rental_ms.repository.RentalRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static vrs.rental_ms.enums.ErrorMessages.RENTAL_NOT_FOUND;
import static vrs.rental_ms.enums.ErrorMessages.VEHICLE_NOT_AVAILABLE;
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

    public RentalResponseDTO createRental(final RentalRequestDTO rentalRequestDTO) {
        var vehicle = getRentalVehicle(rentalRequestDTO.getVehicleId());
        var user = getRentalUser(rentalRequestDTO.getUserId());
        var finalPrice = getFinalPrice(vehicle.getPriceBrl(), rentalRequestDTO.getStartDate(), rentalRequestDTO.getEndDate());

        var rental = rentalRepository.save(
                rentalMapper.toCreateRentalDocument(rentalRequestDTO, vehicle, user, finalPrice));

        rentalProducer.sendToProcessRentalQueue(rentalMapper.toRentalMessageDTO(rental, finalPrice, rentalRequestDTO, user));

        return rentalMapper.toRentalResponseDTO(rental);
    }

    public void processRental(final RentalMessageDTO rentalMessageDTO) {
        var userAddress = securityService.findUserAddressById(rentalMessageDTO.getUser().getId(), rentalMessageDTO.getAddressId());
        var paymentResponseDTO = paymentService.processPayment(rentalMessageDTO, userAddress);

        var rentalStatus = this.updateRentalStatus(rentalMessageDTO.getRentalId(), paymentResponseDTO);
        this.updateVehicleStatus(rentalMessageDTO.getVehicleId(), rentalStatus);
    }

    private RentalStatus updateRentalStatus(final String rentalId, final PaymentResponseDTO paymentResponseDTO) {
        var rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new NotFoundException(RENTAL_NOT_FOUND.getMessage()));

        var rentalStatus = RentalStatus.fromPaymentStatus(paymentResponseDTO.getStatus());

        rental.setPaymentStatus(paymentResponseDTO.getStatus());
        rental.setStatus(rentalStatus);

        rentalRepository.save(rental);

        return rentalStatus;
    }

    private void updateVehicleStatus(final Long vehicleId, final RentalStatus rentalStatus) {
        vehicleService.updateVehicleStatus(vehicleId, VehicleStatus.fromRentalStatus(rentalStatus));
    }

    private VehicleResponseDTO getRentalVehicle(final Long vehicleId) {
        return Optional.of(vehicleService.findVehicleById(vehicleId))
                .filter(vehicleResponseDTO -> vehicleResponseDTO.getStatus().equals(AVAILABLE))
                .orElseThrow(() -> new BadRequestException(VEHICLE_NOT_AVAILABLE.getMessage()));
    }

    private UserResponseDTO getRentalUser(final Long userId) {
        return securityService.findUserById(userId);
    }

    private BigDecimal getFinalPrice(final BigDecimal originalPrice, final Long rentalStartDate, final Long rentalEndDate) {
        var startDate = Instant.ofEpochMilli(rentalStartDate).atZone(ZoneId.systemDefault()).toLocalDate();
        var endDate = Instant.ofEpochMilli(rentalEndDate).atZone(ZoneId.systemDefault()).toLocalDate();
        var daysCount = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        return originalPrice.multiply(new BigDecimal(2L)).multiply(new BigDecimal(daysCount));
    }

}
