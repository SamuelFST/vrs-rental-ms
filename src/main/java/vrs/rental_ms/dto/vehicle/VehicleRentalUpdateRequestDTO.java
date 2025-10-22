package vrs.rental_ms.dto.vehicle;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import vrs.rental_ms.enums.VehicleStatus;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class VehicleRentalUpdateRequestDTO {

    @NotNull(message = "The status must be informed")
    private VehicleStatus status;

    @NotNull(message = "The kmDriven must be informed")
    private BigDecimal kmDriven;

}
