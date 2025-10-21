package vrs.rental_ms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
    PF("PESSOA FÍSICA"),
    PJ("PESSOA JURÍDICA");

    private final String description;

}
