package vrs.rental_ms.dto.rental;

import java.math.BigDecimal;

public record RentalPriceResponse(
        BigDecimal price
) {
}
