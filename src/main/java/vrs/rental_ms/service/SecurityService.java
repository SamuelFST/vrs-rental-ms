package vrs.rental_ms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vrs.rental_ms.dto.user.UserAddressResponseDTO;
import vrs.rental_ms.dto.user.UserResponseDTO;
import vrs.rental_ms.integration.SecurityClient;

@Service
@AllArgsConstructor
public class SecurityService {

    private final SecurityClient securityClient;

    public UserResponseDTO findUserById(final Long userId) {
        return securityClient.findUserById(userId);
    }

    public UserAddressResponseDTO findUserAddressById(final Long userId, final Long addressId) {
        return securityClient.findUserAddressById(userId, addressId);
    }

}
