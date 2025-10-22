package vrs.rental_ms.dto.rental;

import lombok.Data;
import vrs.rental_ms.enums.RentalStatus;

@Data
public class RentalFilterDTO {

    private RentalStatus status;
    private String licensePlate;
    private String email;

}
