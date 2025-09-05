package vrs.rental_ms.config.feign;

import feign.Logger;
import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vrs.rental_ms.interceptor.SecurityTokenInterceptor;

@Configuration
@AllArgsConstructor
public class FeignClientConfiguration {

    private final SecurityTokenInterceptor securityTokenInterceptor;

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return this.securityTokenInterceptor;
    }

}
