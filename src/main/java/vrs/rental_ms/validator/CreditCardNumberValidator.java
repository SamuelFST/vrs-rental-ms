package vrs.rental_ms.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vrs.rental_ms.config.validation.ValidCreditCardNumber;

public class CreditCardNumberValidator implements ConstraintValidator<ValidCreditCardNumber, String> {

    private static final int DECIMAL_BASE = 10;

    /**
     * Validates credit card number using luhn algorithm
     * @param cardNumber the card number to be validated
     * @param context the validation context provided by the Bean Validation framework
     * @return {@code true} if the credit card number is valid; {@code false} otherwise
     * */
    @Override
    public boolean isValid(String cardNumber, ConstraintValidatorContext context) {
        if (cardNumber == null || cardNumber.isBlank()) {
            return true;
        }

        var sum = 0;
        var alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            var digit = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit >= DECIMAL_BASE) {
                    digit = (digit % DECIMAL_BASE) + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }
}
