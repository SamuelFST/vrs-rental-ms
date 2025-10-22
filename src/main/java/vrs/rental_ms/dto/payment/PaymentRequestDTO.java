package vrs.rental_ms.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class PaymentRequestDTO {

    @NotBlank(message = "The apiKey must be informed")
    @JsonProperty(value = "api_key")
    private String apiKey;

    @NotNull(message = "The amount must be informed")
    private BigDecimal amount;

    @NotBlank(message = "The paymentMethod must be informed")
    @JsonProperty(value = "payment_method")
    private String paymentMethod;

    @NotBlank(message = "The cardNumber must be informed")
    @JsonProperty(value = "card_number")
    private String cardNumber;

    @NotBlank(message = "The cardHolderName must be informed")
    @JsonProperty(value = "card_holder_name")
    private String cardHolderName;

    @NotBlank(message = "The cardExpirationMonth must be informed")
    @JsonProperty(value = "card_expiration_month")
    private String cardExpirationMonth;

    @NotBlank(message = "The cardExpirationYear must be informed")
    @JsonProperty(value = "card_expiration_year")
    private String cardExpirationYear;

    @NotBlank(message = "The cardCvv must be informed")
    @JsonProperty(value = "card_cvv")
    private String cardCvv;

    @NotBlank(message = "The installments must be informed")
    private int installments;

    @NotNull(message = "The payment customer must be informed")
    private CustomerDataDTO customer;

}
