package vrs.rental_ms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum VehicleStatus {
    AVAILABLE(RentalStatus.CANCELLED),
    UNDER_MAINTENANCE(null),
    RENTED(RentalStatus.APPROVED);

    private final RentalStatus associatedRentalStatus;

    public static VehicleStatus fromRentalStatus(final RentalStatus rentalStatus) {
        if (rentalStatus == null) {
            return AVAILABLE;
        }

        return Stream.of(values())
                .filter(status -> rentalStatus.equals(status.getAssociatedRentalStatus()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("%s %s".formatted("Equivalent VehicleStatus not found for RentalStatus", rentalStatus)));
    }

}
