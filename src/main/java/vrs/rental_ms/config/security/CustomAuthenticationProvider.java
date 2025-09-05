package vrs.rental_ms.config.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import vrs.rental_ms.service.SecurityService;

import java.util.List;

import static vrs.rental_ms.constants.Constants.ROLE_PREFIX;
import static vrs.rental_ms.enums.ErrorMessages.TOKEN_NOT_INFORMED;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final SecurityService securityService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var accessToken = (String) authentication.getCredentials();

        if (accessToken == null || accessToken.isEmpty()) {
            throw new BadCredentialsException(TOKEN_NOT_INFORMED.getMessage());
        }

        var tokenResponse = securityService.validateToken(accessToken);
        var authorities = List.of(new SimpleGrantedAuthority("%s%s".formatted(ROLE_PREFIX, tokenResponse.getGroup())));

        return new UsernamePasswordAuthenticationToken(
                new User(tokenResponse.getUser().getEmail(), "", authorities),
                null,
                authorities
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}