package vrs.rental_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import vrs.rental_ms.config.properties.AwsProperties;
import vrs.rental_ms.config.properties.PaymentProperties;
import vrs.rental_ms.config.properties.SecurityProperties;

@EnableFeignClients
@SpringBootApplication
@EnableConfigurationProperties(value = { AwsProperties.class, SecurityProperties.class, PaymentProperties.class })
public class RentalMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalMsApplication.class, args);
	}

}
