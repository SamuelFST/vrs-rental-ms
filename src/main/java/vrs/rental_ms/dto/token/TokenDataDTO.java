package vrs.rental_ms.dto.token;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import vrs.rental_ms.dto.user.UserResponseDTO;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TokenDataDTO {

    private UserResponseDTO user;
    private String group;

}
