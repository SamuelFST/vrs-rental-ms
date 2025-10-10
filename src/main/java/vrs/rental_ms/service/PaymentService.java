package vrs.rental_ms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vrs.rental_ms.config.properties.PaymentProperties;
import vrs.rental_ms.dto.payment.PaymentResponseDTO;
import vrs.rental_ms.dto.rental.RentalMessageDTO;
import vrs.rental_ms.dto.user.UserAddressResponseDTO;
import vrs.rental_ms.integration.PaymentClient;
import vrs.rental_ms.mapper.PaymentMapper;

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

}
