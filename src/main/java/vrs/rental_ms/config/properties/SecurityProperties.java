package vrs.rental_ms.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("rental-ms.integration.security-client")
public class SecurityProperties {

    private String serviceId;
    private String serviceSecret;

}
