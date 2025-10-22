package vrs.rental_ms.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import vrs.rental_ms.service.SecurityAuthService;

public class SecurityTokenInterceptor implements RequestInterceptor {

    private final SecurityAuthService securityAuthService;

    public SecurityTokenInterceptor(SecurityAuthService securityAuthService) {
        this.securityAuthService = securityAuthService;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("accessToken", securityAuthService.generateTokenForService().getAccessToken());
    }

}