package vrs.rental_ms.dto.rental;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import vrs.rental_ms.dto.rental.cc.CreditCardDTO;
import vrs.rental_ms.dto.user.UserResponseDTO;
import vrs.rental_ms.enums.DocumentType;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class RentalMessageDTO {

    @NotBlank(message = "The rentalId is required")
    private String rentalId;

    @Valid
    @NotNull(message = "The creditCardData is required")
    private CreditCardDTO creditCardData;

    @NotNull
    private Long vehicleId;

    @NotNull
    private Long addressId;

    @NotNull
    private UserResponseDTO user;

    @NotNull
    private BigDecimal totalPrice;

    @NotNull(message = "The documentType is required")
    private DocumentType documentType;

    @NotBlank(message = "The documentNumber is required")
    private String documentNumber;

}
