package vrs.rental_ms.dto.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomerAdressDataDTO {

    @NotBlank(message = "The street must be informed")
    private String street;

    @NotBlank(message = "The number must be informed")
    private String number;

    @NotBlank(message = "The neighborhood must be informed")
    private String neighborhood;

    @NotBlank(message = "The city must be informed")
    private String city;

    @NotBlank(message = "The state must be informed")
    private String state;

    @NotBlank(message = "The street code must be informed")
    private String zipcode;

    @NotBlank(message = "The country must be informed")
    private String country;

}
