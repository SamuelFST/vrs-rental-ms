package vrs.rental_ms.config.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static vrs.rental_ms.constants.Constants.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(HAS_ROLE_START + CLIENT + HAS_ROLE_END + HAS_ROLE_OR + HAS_ROLE_START + ADMIN + HAS_ROLE_END)
public @interface IsClient {

}