package vrs.rental_ms.mapper;

import org.mapstruct.*;
import vrs.rental_ms.document.Rental;
import vrs.rental_ms.document.subdocument.VehicleData;
import vrs.rental_ms.dto.rental.*;
import vrs.rental_ms.dto.user.UserResponseDTO;
import vrs.rental_ms.dto.vehicle.VehicleResponseDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring", imports = { Instant.class })
public interface RentalMapper {

    @AfterMapping
    default void updateCreateRentalDocument(@MappingTarget Rental rental,
                                            RentalRequestDTO rentalRequestDTO,
                                            VehicleResponseDTO vehicleResponseDTO) {
        rental.setVehicleData(new VehicleData()
                .setId(vehicleResponseDTO.getId())
                .setBrand(vehicleResponseDTO.getBrand().getName())
                .setModel(vehicleResponseDTO.getModel().getName())
                .setVersion(vehicleResponseDTO.getVersion().getName())
                .setYear(vehicleResponseDTO.getYear())
                .setLicensePlate(vehicleResponseDTO.getLicensePlate()));

        rental.getUserData().setDocumentNumber(rentalRequestDTO.getDocumentNumber());
        rental.getUserData().setDocumentType(rentalRequestDTO.getDocumentType());

        var creditCardNumber = rentalRequestDTO.getCreditCardData().getNumber();

        rental.setCcLastNumbers(creditCardNumber.substring(creditCardNumber.length() - 4));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "returnedValue", ignore = true)
    @Mapping(target = "receivedBackDate", ignore = true)
    @Mapping(target = "rentalKmDriven", ignore = true)
    @Mapping(target = "generatedContract", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "paymentStatus", constant = "PROCESSING")
    @Mapping(target = "userData", source = "userResponseDTO")
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    Rental toCreateRentalDocument(RentalRequestDTO rentalRequestDTO,
                                  VehicleResponseDTO vehicleResponseDTO,
                                  UserResponseDTO userResponseDTO,
                                  BigDecimal finalPrice);

    RentalResponseDTO toRentalResponseDTO(Rental rental);

    List<RentalResponseDTO> toRentalResponseDTOList(List<Rental> rental);

    @Mapping(target = "rentalId", source = "rental.id")
    @Mapping(target = "totalPrice", source = "finalPrice")
    @Mapping(target = "documentNumber", source = "rentalRequestDTO.documentNumber")
    @Mapping(target = "documentType", source = "rentalRequestDTO.documentType")
    @Mapping(target = "addressId", source = "rental.addressId")
    RentalMessageDTO toRentalMessageDTO(Rental rental,
                                        BigDecimal finalPrice,
                                        RentalRequestDTO rentalRequestDTO,
                                        UserResponseDTO user);

    RentalFinishMessageDTO toRentalFinishMessageDTO(String rentalId,
                                                    RentalFinishRequestDTO rentalFinishRequestDTO);

}
