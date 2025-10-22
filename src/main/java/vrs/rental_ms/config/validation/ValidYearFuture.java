package vrs.rental_ms.config.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import vrs.rental_ms.validator.YearFutureValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = YearFutureValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidYearFuture {
    String message() default "The year must be in future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}