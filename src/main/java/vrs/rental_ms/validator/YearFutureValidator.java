package vrs.rental_ms.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vrs.rental_ms.config.validation.ValidYearFuture;

import java.time.Year;

public class YearFutureValidator implements ConstraintValidator<ValidYearFuture, Integer> {

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return false;
        }

        return year > Year.now().getValue();
    }
}