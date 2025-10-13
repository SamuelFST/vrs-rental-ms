package vrs.rental_ms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vrs.rental_ms.config.properties.PaymentProperties;
import vrs.rental_ms.dto.payment.PaymentResponseDTO;
import vrs.rental_ms.dto.payment.RefundRequestDTO;
import vrs.rental_ms.dto.rental.RentalMessageDTO;
import vrs.rental_ms.dto.user.UserAddressResponseDTO;
import vrs.rental_ms.integration.PaymentClient;
import vrs.rental_ms.mapper.PaymentMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static vrs.rental_ms.constants.Constants.BIG_DECIMAL_ZERO;
import static vrs.rental_ms.constants.Constants.PAYMENT_FACTOR;

@Service
@AllArgsConstructor
public class PaymentService {

    private final PaymentClient paymentClient;
    private final PaymentProperties paymentProperties;
    private final PaymentMapper paymentMapper;

    public PaymentResponseDTO processPayment(final RentalMessageDTO rentalMessageDTO,
                                             final UserAddressResponseDTO userAddressResponseDTO) {
        return paymentClient.payRental(paymentMapper.toPaymentRequestDTO(rentalMessageDTO, userAddressResponseDTO, paymentProperties));
    }

    public PaymentResponseDTO refundPayment(final BigDecimal value,
                                            final String transactionId) {
        return paymentClient.refundRental(transactionId,
                new RefundRequestDTO()
                        .setAmount(value)
                        .setApiKey(paymentProperties.getApiKey()));
    }

    public BigDecimal calculateValueToBeReturned(final Long daysCount, final BigDecimal finalPrice) {
        var deductiblePrice = finalPrice.divide(PAYMENT_FACTOR, 2, RoundingMode.HALF_UP);

        if (daysCount > 0L) {
            var factor = paymentProperties.getLateFeeMultiplier()
                    .pow(daysCount.intValue());

            var calculatedValue = deductiblePrice.multiply(factor).setScale(2, RoundingMode.HALF_UP);

            if (calculatedValue.compareTo(finalPrice) >= 0) {
                return BIG_DECIMAL_ZERO;
            }

            return finalPrice.subtract(calculatedValue);
        }

        return deductiblePrice;
    }

}
