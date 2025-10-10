package vrs.rental_ms.dto.user;

import lombok.Data;

@Data
public class UserAddressResponseDTO {

    private Long id;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String code;
    private String contactPhone;
    private String country;

}
