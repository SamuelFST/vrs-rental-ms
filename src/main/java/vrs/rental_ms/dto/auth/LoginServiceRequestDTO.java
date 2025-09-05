package vrs.rental_ms.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginServiceRequestDTO {

    @NotBlank(message = "The serviceId must be informed")
    private String serviceId;

    @NotBlank(message = "The serviceSecret must be informed")
    private String serviceSecret;

}
