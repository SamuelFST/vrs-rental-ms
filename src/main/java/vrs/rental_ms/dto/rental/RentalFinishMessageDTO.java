package vrs.rental_ms.dto.rental;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class RentalFinishMessageDTO {

    @NotBlank(message = "The rentalId is required")
    private String rentalId;

    @NotNull(message = "The receivedBackDate is required")
    private Long receivedBackDate;

    @NotNull(message = "The rentalKmDriven is required")
    private BigDecimal rentalKmDriven;

}
