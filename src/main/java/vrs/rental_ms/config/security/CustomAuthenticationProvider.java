package vrs.rental_ms.config.security;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import vrs.rental_ms.service.SecurityAuthService;

import java.util.List;

import static vrs.rental_ms.constants.Constants.ROLE_PREFIX;
import static vrs.rental_ms.enums.ErrorMessages.TOKEN_NOT_INFORMED;

@Slf4j
@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final SecurityAuthService securityAuthService;

    private static final String TOKEN_SERVICE_FAILURE = "Token validation service failure";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var accessToken = (String) authentication.getCredentials();

        if (!StringUtils.hasText(accessToken)) {
            throw new BadCredentialsException(TOKEN_NOT_INFORMED.getMessage());
        }

        try {
            var tokenResponse = securityAuthService.validateToken(accessToken);

            if (tokenResponse == null || tokenResponse.getGroup() == null) {
                throw new BadCredentialsException("Invalid token data");
            }

            var authorities = List.of(
                    new SimpleGrantedAuthority("%s%s".formatted(ROLE_PREFIX, tokenResponse.getGroup()))
            );

            return new UsernamePasswordAuthenticationToken(
                    new User(tokenResponse.getUser().getEmail(), "", authorities),
                    null,
                    authorities
            );
        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            throw new BadCredentialsException("Invalid token", ex);
        } catch (FeignException ex) {
            log.error(TOKEN_SERVICE_FAILURE, ex);
            throw new AuthenticationServiceException(TOKEN_SERVICE_FAILURE, ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}