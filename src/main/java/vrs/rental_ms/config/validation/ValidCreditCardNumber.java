package vrs.rental_ms.config.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import vrs.rental_ms.validator.CreditCardNumberValidator;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CreditCardNumberValidator.class)
public @interface ValidCreditCardNumber {
    String message() default "The credit card number is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
