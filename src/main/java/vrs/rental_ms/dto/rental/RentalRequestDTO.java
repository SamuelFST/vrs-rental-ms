package vrs.rental_ms.dto.rental;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import vrs.rental_ms.dto.rental.cc.CreditCardDTO;
import vrs.rental_ms.enums.DocumentType;

@Data
@Accessors(chain = true)
public class RentalRequestDTO {

    @NotNull(message = "The vehicleId is required")
    private Long vehicleId;

    @NotNull(message = "The userId is required")
    private Long userId;

    @NotNull(message = "The addressId is required")
    private Long addressId;

    @NotNull(message = "The startDate is required")
    private Long startDate;

    @NotNull(message = "The endDate is required")
    private Long endDate;

    @Valid
    @NotNull(message = "The creditCardData is required")
    private CreditCardDTO creditCardData;

    @NotNull(message = "The documentType is required")
    private DocumentType documentType;

    @NotBlank(message = "The documentNumber is required")
    private String documentNumber;

}
