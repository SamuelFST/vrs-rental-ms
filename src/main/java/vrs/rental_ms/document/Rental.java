package vrs.rental_ms.document;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import vrs.rental_ms.document.subdocument.UserData;
import vrs.rental_ms.document.subdocument.VehicleData;
import vrs.rental_ms.enums.PaymentStatus;
import vrs.rental_ms.enums.RentalStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Document(collection = "rentals")
public class Rental {

    @Id
    private String id;

    @NotNull(message = "The vehicleData is required")
    private VehicleData vehicleData;

    @NotNull(message = "The userData is required")
    private UserData userData;

    @NotNull(message = "The finalPrice is required")
    private BigDecimal finalPrice;

    private BigDecimal returnedValue;

    @NotNull(message = "The paymentStatus is required")
    private PaymentStatus paymentStatus;

    private String paymentTransactionId;

    @NotNull(message = "The startDate is required")
    private Long startDate;

    @NotNull(message = "The endDate is required")
    private Long endDate;

    private Long receivedBackDate;

    private BigDecimal rentalKmDriven;

    @NotNull(message = "The rentalStatus is required")
    private RentalStatus status;

    private String generatedContract;

    @NotNull(message = "The ccLastNumbers is required")
    private String ccLastNumbers;

    @NotNull(message = "The addressId is required")
    private Long addressId;

    @NotNull(message = "The createdAt is required")
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
