package vrs.rental_ms.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import static vrs.rental_ms.constants.Constants.ADMIN;
import static vrs.rental_ms.constants.Constants.ROLE_PREFIX;
import static vrs.rental_ms.enums.ErrorMessages.CURRENT_USER_OPERATION_NOT_ALLOWED;

@UtilityClass
public class UserSecurityUtil {

    public boolean currentUserIsAdmin() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        for (var authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("%s%s".formatted(ROLE_PREFIX, ADMIN))) {
                return true;
            }
        }

        return false;
    }

    public void currentUserIsTheResourceOwner(final String requestUserEmail) {
        var authenticationPrincipal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!authenticationPrincipal.getUsername().equals(requestUserEmail)) {
            throw new AccessDeniedException(CURRENT_USER_OPERATION_NOT_ALLOWED.getMessage());
        }
    }

}
