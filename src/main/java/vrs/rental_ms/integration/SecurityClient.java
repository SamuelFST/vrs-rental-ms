package vrs.rental_ms.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vrs.rental_ms.dto.user.UserAddressResponseDTO;
import vrs.rental_ms.dto.user.UserResponseDTO;
import vrs.rental_ms.interceptor.SecurityTokenInterceptor;

@FeignClient(name = "SecurityClient", url = "${rental-ms.integration.security-client.base-url}", configuration = SecurityTokenInterceptor.class)
public interface SecurityClient {

    @GetMapping("${rental-ms.integration.security-client.find-user-by-id}")
    UserResponseDTO findUserById(@PathVariable final Long userId);

    @GetMapping("${rental-ms.integration.security-client.find-user-address-by-id}")
    UserAddressResponseDTO findUserAddressById(@PathVariable final Long userId, @PathVariable final Long addressId);

}
