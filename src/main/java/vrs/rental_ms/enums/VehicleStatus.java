package vrs.rental_ms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum VehicleStatus {
    AVAILABLE(List.of(RentalStatus.CLOSED, RentalStatus.CANCELLED)),
    UNDER_MAINTENANCE(List.of()),
    RENTED(List.of(RentalStatus.APPROVED));

    private final List<RentalStatus> associatedRentalStatuses;

    public static VehicleStatus fromRentalStatus(final RentalStatus rentalStatus) {
        if (rentalStatus == null) {
            return AVAILABLE;
        }

        return Stream.of(values())
                .filter(status -> status.getAssociatedRentalStatuses().contains(rentalStatus))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("%s %s".formatted("Equivalent VehicleStatus not found for RentalStatus", rentalStatus)));
    }

}
