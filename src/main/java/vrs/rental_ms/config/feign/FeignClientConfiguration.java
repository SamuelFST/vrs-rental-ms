package vrs.rental_ms.config.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vrs.rental_ms.interceptor.SecurityTokenInterceptor;
import vrs.rental_ms.service.SecurityAuthService;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public SecurityTokenInterceptor securityTokenInterceptor(SecurityAuthService securityAuthService) {
        return new SecurityTokenInterceptor(securityAuthService);
    }

}
