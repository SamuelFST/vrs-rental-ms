package vrs.rental_ms.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import vrs.rental_ms.service.SecurityService;

@Component
public class SecurityTokenInterceptor implements RequestInterceptor {

    private final SecurityService securityService;

    public SecurityTokenInterceptor(@Lazy SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("accessToken", securityService.generateTokenForService().getAccessToken());
    }
}