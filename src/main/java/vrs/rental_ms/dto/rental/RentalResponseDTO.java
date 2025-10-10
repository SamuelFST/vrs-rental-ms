package vrs.rental_ms.dto.rental;

import lombok.Data;
import vrs.rental_ms.document.subdocument.UserData;
import vrs.rental_ms.document.subdocument.VehicleData;
import vrs.rental_ms.enums.PaymentStatus;
import vrs.rental_ms.enums.RentalStatus;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RentalResponseDTO {

    private String id;
    private VehicleData vehicleData;
    private UserData userData;
    private BigDecimal finalPrice;
    private BigDecimal returnedValue;
    private PaymentStatus paymentStatus;
    private Long startDate;
    private Long endDate;
    private Long receivedBackDate;
    private BigDecimal rentalKmDriven;
    private RentalStatus status;
    private String generatedContract;
    private String ccLastNumbers;
    private Long addressId;
    private Date createdAt;

}
