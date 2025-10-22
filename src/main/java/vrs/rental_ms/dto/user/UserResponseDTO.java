package vrs.rental_ms.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;
import vrs.rental_ms.enums.UserType;

@Data
@Accessors(chain = true)
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private UserType userType;
    private String group;

}
