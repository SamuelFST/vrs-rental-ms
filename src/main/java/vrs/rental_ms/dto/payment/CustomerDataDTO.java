package vrs.rental_ms.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import vrs.rental_ms.enums.CustomerDocumentType;

import java.util.List;

@Data
@Accessors(chain = true)
public class CustomerDataDTO {

    @NotNull(message = "The customer id must be informed")
    private Long id;

    @NotBlank(message = "The customer name must be informed")
    private String name;

    @NotBlank(message = "The customer email must be informed")
    private String email;

    @NotNull(message = "The customer document type must be informed")
    private CustomerDocumentType type;

    @NotBlank(message = "The customer document must be informed")
    private String document;

    @NotNull(message = "The customer phone numbers must be informed")
    @JsonProperty(value = "phone_numbers")
    private List<String> phoneNumbers;

    @NotNull(message = "The customer address must be informed")
    private CustomerAdressDataDTO address;

}
