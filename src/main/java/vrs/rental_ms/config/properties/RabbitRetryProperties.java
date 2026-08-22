package vrs.rental_ms.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "rental-ms.rabbit.retry")
public record RabbitRetryProperties(
        Duration initialInterval,
        double multiplier,
        Duration maxInterval,
        int maxRetries
) {
}
