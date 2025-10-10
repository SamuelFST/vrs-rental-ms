package vrs.rental_ms.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import vrs.rental_ms.dto.auth.LoginResponseDTO;
import vrs.rental_ms.dto.auth.LoginServiceRequestDTO;
import vrs.rental_ms.dto.token.TokenDataDTO;

@FeignClient(name = "SecurityAuthClient", url = "${rental-ms.integration.security-auth-client.base-url}")
public interface SecurityAuthClient {

    @PostMapping("${rental-ms.integration.security-auth-client.validate-token}")
    TokenDataDTO validateToken(@RequestHeader final String accessToken);

    @PostMapping("${rental-ms.integration.security-auth-client.generate-token-service}")
    LoginResponseDTO generateTokenForService(@RequestBody final LoginServiceRequestDTO loginServiceRequestDTO);

}
