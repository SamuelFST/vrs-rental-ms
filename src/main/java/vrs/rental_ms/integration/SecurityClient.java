package vrs.rental_ms.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import vrs.rental_ms.dto.auth.LoginResponseDTO;
import vrs.rental_ms.dto.auth.LoginServiceRequestDTO;
import vrs.rental_ms.dto.token.TokenDataDTO;

@FeignClient(name = "SecurityClient", url = "${rental-ms.integration.security-client.base-url}")
public interface SecurityClient {

    @PostMapping("${rental-ms.integration.security-client.validate-token}")
    TokenDataDTO validateToken(@RequestHeader final String accessToken);

    @PostMapping("${rental-ms.integration.security-client.generate-token-service}")
    LoginResponseDTO generateTokenForService(@RequestBody final LoginServiceRequestDTO loginServiceRequestDTO);

}
