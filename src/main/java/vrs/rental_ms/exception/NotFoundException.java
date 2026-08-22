package vrs.rental_ms.exception;

import java.io.Serial;

public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -9178858898784350713L;

    public NotFoundException(String message) {
        super(message);
    }

}
