package vrs.rental_ms.integration.fallback;

import org.springframework.stereotype.Component;
import vrs.rental_ms.dto.payment.PaymentRequestDTO;
import vrs.rental_ms.dto.payment.PaymentResponseDTO;
import vrs.rental_ms.enums.PaymentStatus;
import vrs.rental_ms.integration.PaymentClient;

import java.time.LocalDateTime;

@Component
public class PaymentClientFallback implements PaymentClient {

    @Override
    public PaymentResponseDTO payRental(PaymentRequestDTO paymentRequestDTO) {
        return new PaymentResponseDTO()
                .setStatus(PaymentStatus.APPROVED)
                .setPaidAt(LocalDateTime.now())
                .setCardLastDigits(paymentRequestDTO.getCardNumber()
                        .substring(paymentRequestDTO.getCardNumber().length() - 4));
    }

}
