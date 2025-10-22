package vrs.rental_ms.dto.auth;

import lombok.Data;
import lombok.experimental.Accessors;
import vrs.rental_ms.dto.user.UserResponseDTO;

@Data
@Accessors(chain = true)
public class LoginResponseDTO {

    private String accessToken;
    private UserResponseDTO user;
    private String group;

}
