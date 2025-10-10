package vrs.rental_ms.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vrs.rental_ms.dto.payment.PaymentRequestDTO;
import vrs.rental_ms.dto.payment.PaymentResponseDTO;
import vrs.rental_ms.integration.fallback.PaymentClientFallback;

@FeignClient(
        name = "PaymentClient",
        url = "${rental-ms.integration.payment-client.base-url}",
        fallback = PaymentClientFallback.class)
public interface PaymentClient {

    @PostMapping("${rental-ms.integration.payment-client.pay-rental}")
    PaymentResponseDTO payRental(@RequestBody final PaymentRequestDTO paymentRequestDTO);

}
