package vrs.rental_ms.dto.rental.cc;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.Accessors;
import vrs.rental_ms.config.validation.ValidCreditCardNumber;
import vrs.rental_ms.config.validation.ValidYearFuture;

@Data
@Accessors(chain = true)
public class CreditCardDTO {

    @ValidCreditCardNumber
    @NotBlank(message = "The card number must be informed")
    @Size(min = 13, max = 19, message = "The card number must have between 13 and 19 digits")
    private String number;

    @NotBlank(message = "The card holderName must be informed")
    private String holderName;

    @NotNull(message = "The card expirationMonth must be informed")
    @Min(value = 1, message = "The card expirationMonth must be between 1 and 12")
    @Max(value = 12, message = "The card expirationMonth must be between 1 and 12.")
    private Integer expirationMonth;

    @ValidYearFuture
    @NotNull(message = "The card expirationYear must be informed")
    private Integer expirationYear;

    @NotBlank(message = "The card cvv must be informed")
    @Pattern(regexp = "\\d{3,4}", message = "The card cvv must have between 3 and 4 digits")
    private String cvv;

}
