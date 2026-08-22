package vrs.rental_ms.exception;

import java.io.Serial;

public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7410843750090232498L;

    public BadRequestException(String message) {
        super(message);
    }

}
