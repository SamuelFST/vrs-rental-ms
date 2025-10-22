package vrs.rental_ms.constants;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class Constants {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String HAS_ROLE_START = "hasRole('";
    public static final String HAS_ROLE_END = "')";
    public static final String HAS_ROLE_OR = " or ";
    public static final String ADMIN = "ADMIN";
    public static final String CLIENT = "CLIENT";

    public static final Long ONE_DAY_IN_SECONDS = 86400L;

    public static final HttpStatus INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    public static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    public static final HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;
    public static final HttpStatus FORBIDDEN = HttpStatus.FORBIDDEN;
    public static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;

    public static final BigDecimal BIG_DECIMAL_ZERO = BigDecimal.ZERO;
    public static final BigDecimal PAYMENT_FACTOR = new BigDecimal(2L);

}
