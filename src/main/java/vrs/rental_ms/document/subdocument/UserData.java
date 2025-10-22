package vrs.rental_ms.document.subdocument;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import vrs.rental_ms.enums.DocumentType;
import vrs.rental_ms.enums.UserType;

@Data
@Accessors(chain = true)
public class UserData {

    @NotNull(message = "The user id must be informed")
    private Long id;

    @NotBlank(message = "The user name must be informed")
    private String name;

    @NotBlank(message = "The user name must be informed")
    private String email;

    @NotBlank(message = "The document number must be informed")
    private String documentNumber;

    @NotNull(message = "The documentType must be informed")
    private DocumentType documentType;

    @NotNull(message = "The userType must be informed")
    private UserType userType;

}
