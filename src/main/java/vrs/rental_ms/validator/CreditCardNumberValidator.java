package vrs.rental_ms.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vrs.rental_ms.config.validation.ValidCreditCardNumber;

public class CreditCardNumberValidator implements ConstraintValidator<ValidCreditCardNumber, String> {

    @Override
    public boolean isValid(String cardNumber, ConstraintValidatorContext context) {
        // validates credit card number using luhn algorithm
        if (cardNumber == null || cardNumber.isBlank()) {
            return true;
        }

        var sum = 0;
        var alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            var digit = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }
}
