package vrs.rental_ms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

import static vrs.rental_ms.enums.UserType.PF;
import static vrs.rental_ms.enums.UserType.PJ;

@Getter
@AllArgsConstructor
public enum CustomerDocumentType {
    INDIVIDUAL("individual", PF),
    COMPANY("company", PJ);

    private final String value;
    private final UserType associatedUserType;

    public static CustomerDocumentType fromUserType(final UserType userType) {
        return Stream.of(values())
                .filter(type -> userType.equals(type.associatedUserType))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("%s %s".formatted("Equivalent CustomerDocumentType not found for UserType", userType)));
    }

}
