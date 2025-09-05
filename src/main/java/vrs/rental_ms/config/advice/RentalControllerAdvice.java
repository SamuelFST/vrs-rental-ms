package vrs.rental_ms.config.advice;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vrs.rental_ms.exception.BadRequestException;
import vrs.rental_ms.exception.NotFoundException;

import java.time.LocalDateTime;

import static vrs.rental_ms.constants.Constants.*;
import static vrs.rental_ms.enums.ErrorMessages.UNEXPECTED_ERROR;

@Slf4j
@ControllerAdvice
public class RentalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGlobalException(Exception ex) {
        log.error(UNEXPECTED_ERROR.getMessage(), ex);

        return new ResponseEntity<>(generateProblemDetail(ex, INTERNAL_SERVER_ERROR, "Unexpected Error"), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ProblemDetail> handleFeignException(FeignException ex) {
        return new ResponseEntity<>(generateProblemDetail(ex, HttpStatus.valueOf(ex.status()), ex.getMessage()), HttpStatus.valueOf(ex.status()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(generateProblemDetail(ex, BAD_REQUEST, ex.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ProblemDetail> handleMissingRequestHeaderException(
            MissingRequestHeaderException ex) {
        return new ResponseEntity<>(generateProblemDetail(ex, BAD_REQUEST, ex.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(generateProblemDetail(ex, BAD_REQUEST, ex.getTitleMessageCode()), BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(
            BadCredentialsException ex) {
        return new ResponseEntity<>(generateProblemDetail(ex, UNAUTHORIZED, ex.getMessage()), UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(
            AccessDeniedException ex) {
        return new ResponseEntity<>(generateProblemDetail(ex, FORBIDDEN, ex.getMessage()), FORBIDDEN);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoResourceFoundException(
            NoResourceFoundException ex) {
        return new ResponseEntity<>(generateProblemDetail(ex, NOT_FOUND, ex.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetail> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        return new ResponseEntity<>(generateProblemDetail(ex, BAD_REQUEST, ex.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> handleBadRequestException(
            BadRequestException ex) {
        return new ResponseEntity<>(generateProblemDetail(ex, BAD_REQUEST, ex.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFoundException(
            NotFoundException ex) {
        return new ResponseEntity<>(generateProblemDetail(ex, NOT_FOUND, ex.getMessage()), NOT_FOUND);
    }

    private ProblemDetail generateProblemDetail(final Exception ex,
                                                final HttpStatus status,
                                                final String detail) {
        log.error("An error occurred: {}", ex.getMessage());

        var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            problemDetail.setProperty("errors", methodArgumentNotValidException.getBindingResult().getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList());
        }

        return problemDetail;
    }

}
