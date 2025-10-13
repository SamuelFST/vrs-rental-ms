package vrs.rental_ms.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class RefundRequestDTO {

    @NotBlank(message = "The apiKey must be informed")
    @JsonProperty(value = "api_key")
    private String apiKey;

    @NotNull(message = "The amount must be informed")
    private BigDecimal amount;

}
