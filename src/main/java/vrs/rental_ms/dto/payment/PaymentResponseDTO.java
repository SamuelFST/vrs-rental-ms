package vrs.rental_ms.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import vrs.rental_ms.enums.PaymentStatus;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class PaymentResponseDTO {

    private PaymentStatus status;

    @JsonProperty(value = "paid_at")
    private LocalDateTime paidAt;

    @JsonProperty(value = "card_last_digits")
    private String cardLastDigits;

    @JsonProperty(value = "card_last_digits")
    private String transactionId;

}
