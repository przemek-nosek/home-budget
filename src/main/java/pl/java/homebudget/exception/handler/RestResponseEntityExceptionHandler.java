package pl.java.homebudget.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.java.homebudget.exception.AssetNotFoundException;
import pl.java.homebudget.exception.dto.ErrorMessage;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String CONSTRAINT_VALIDATION_EXCEPTION_MESSAGE = "Validation failed.";

    @ExceptionHandler(AssetNotFoundException.class)
    protected <T extends RuntimeException> ResponseEntity<ErrorMessage> handleNotFoundException(T ex) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage(), Collections.emptyList());

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();

        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            errors.add(constraintViolation.getMessageTemplate());
        }

        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                CONSTRAINT_VALIDATION_EXCEPTION_MESSAGE,
                errors);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
