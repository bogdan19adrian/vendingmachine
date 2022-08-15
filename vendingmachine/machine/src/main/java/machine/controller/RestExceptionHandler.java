package machine.controller;

import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import machine.api.ErrorResponseDTO;
import machine.exception.BadRequestException;
import machine.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * The type Rest exception handler.
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    /**
     * The constant BAD_REQUEST_MESSAGE.
     */
    public static final String BAD_REQUEST_MESSAGE = "Bad request {}";
    /**
     * The constant UNEXPECTED_ERROR.
     */
    public static final String UNEXPECTED_ERROR = "UNEXPECTED_ERROR";

    /**
     * Handle bad request exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex) {
        log.error(ex.getLocalizedMessage(), ex);
        log.info(ex.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handle method argument type mismatch response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage
                .append("Failed to convert the ")
                .append(ex.getValue())
                .append(" value.The type of ")
                .append(ex.getName())
                .append(" is not valid.");

        log.error(ex.getMessage(), ex);
        log.info(BAD_REQUEST_MESSAGE, errorMessage);

        ErrorResponseDTO error = new ErrorResponseDTO(ErrorCode.BAD_REQUEST.name(), errorMessage.toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handle constraint violation response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(final ConstraintViolationException ex) {
        StringBuilder errors = new StringBuilder();

        ex.getConstraintViolations().forEach(
                exception -> errors.append(exception.getMessage()).append(" ")
        );
        errors.deleteCharAt(errors.lastIndexOf(" "));

        log.error(ex.getMessage(), ex);
        log.info(BAD_REQUEST_MESSAGE, errors);

        ErrorResponseDTO error = new ErrorResponseDTO(ErrorCode.BAD_REQUEST.name(), errors.toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle method argument not valid response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.append(error.getDefaultMessage()).append(" ");
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.append(error.getDefaultMessage()).append(" ");
        }

        log.error(ex.getMessage(), ex);
        log.info(BAD_REQUEST_MESSAGE, errors);

        ErrorResponseDTO error = new ErrorResponseDTO(ErrorCode.BAD_REQUEST.name(), errors.toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handle http message not readable exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.info("System error while processing the request!", ex);
        ErrorResponseDTO error = new ErrorResponseDTO(ErrorCode.BAD_REQUEST.name(), "Invalid request.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handle unknown exception error response dto.
     *
     * @param ex the ex
     * @return the error response dto
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponseDTO handleUnknownException(Exception ex) {
        log.error("System error while processing the request!", ex);
        return new ErrorResponseDTO(UNEXPECTED_ERROR, "System error while processing the request");
    }
}
