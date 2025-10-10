package vrs.rental_ms.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("rental-ms.payment")
public class PaymentProperties {

    private String apiKey;
    private String allowedPaymentMethod;
    private int allowedInstallments;

}
