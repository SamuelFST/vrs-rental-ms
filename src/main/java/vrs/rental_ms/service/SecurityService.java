package vrs.rental_ms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vrs.rental_ms.config.properties.SecurityProperties;
import vrs.rental_ms.dto.auth.LoginResponseDTO;
import vrs.rental_ms.dto.auth.LoginServiceRequestDTO;
import vrs.rental_ms.dto.token.TokenDataDTO;
import vrs.rental_ms.integration.SecurityClient;

@Service
@AllArgsConstructor
public class SecurityService {

    private final SecurityClient securityClient;
    private final SecurityProperties securityProperties;

    public TokenDataDTO validateToken(final String accessToken) {
        return securityClient.validateToken(accessToken);
    }

    public LoginResponseDTO generateTokenForService() {
        return securityClient.generateTokenForService(new LoginServiceRequestDTO()
                .setServiceId(securityProperties.getServiceId())
                .setServiceSecret(securityProperties.getServiceSecret()));
    }

}
