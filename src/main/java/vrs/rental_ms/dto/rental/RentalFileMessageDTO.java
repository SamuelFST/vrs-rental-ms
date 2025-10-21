package vrs.rental_ms.dto.rental;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RentalFileMessageDTO {

    @NotBlank(message = "The rentalId is required")
    private String rentalId;

}
