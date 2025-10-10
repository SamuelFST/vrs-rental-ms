package vrs.rental_ms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum RentalStatus {
    APPROVED(PaymentStatus.APPROVED),
    PENDING(PaymentStatus.PROCESSING),
    CLOSED(null),
    CANCELLED(PaymentStatus.REFUSED);

    private final PaymentStatus associatedPaymentStatus;

    public static RentalStatus fromPaymentStatus(final PaymentStatus paymentStatus) {
        if (paymentStatus == null) {
            return CLOSED;
        }

        return Stream.of(values())
                .filter(status -> paymentStatus.equals(status.associatedPaymentStatus))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("%s %s".formatted("Equivalent RentalStatus not found for PaymentStatus", paymentStatus)));
    }

}
