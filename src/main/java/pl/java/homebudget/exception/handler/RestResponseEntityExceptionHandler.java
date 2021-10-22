package pl.java.homebudget.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.java.homebudget.exception.AppUserInvalidUsernameOrPasswordException;
import pl.java.homebudget.exception.AssetNotFoundException;
import pl.java.homebudget.exception.ExpenseNotFoundException;
import pl.java.homebudget.exception.UsernameAlreadyExistsException;
import pl.java.homebudget.exception.dto.ErrorMessage;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String CONSTRAINT_VALIDATION_EXCEPTION_MESSAGE = "Validation failed.";

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorMessage> handleGenericException(Exception ex) {
        ErrorMessage errorMessage = getErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), Collections.emptyList());

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AssetNotFoundException.class, UsernameNotFoundException.class, ExpenseNotFoundException.class})
    protected <T extends RuntimeException> ResponseEntity<ErrorMessage> handleNotFoundException(T ex) {
        ErrorMessage errorMessage = getErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage(), Collections.emptyList());

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }


//    @ExceptionHandler(ConstraintViolationException.class)
//    protected ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException ex) {
//        List<String> errors = new ArrayList<>();
//
//        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
//
//        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
//            errors.add(constraintViolation.getMessageTemplate());
//        }
//
//        ErrorMessage errorMessage = getErrorMessage(HttpStatus.BAD_REQUEST, CONSTRAINT_VALIDATION_EXCEPTION_MESSAGE, errors);
//
//        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(AppUserInvalidUsernameOrPasswordException.class)
    protected ResponseEntity<ErrorMessage> handleAppUserInvalidUsernameOrPasswordException(AppUserInvalidUsernameOrPasswordException ex) {
        ErrorMessage errorMessage = getErrorMessage(HttpStatus.FORBIDDEN, ex.getMessage(), Collections.emptyList());

        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    protected ResponseEntity<ErrorMessage> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        ErrorMessage errorMessage = getErrorMessage(HttpStatus.CONFLICT, ex.getMessage(), Collections.emptyList());

        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    private ErrorMessage getErrorMessage(HttpStatus httpStatus, String message, List<String> errors) {
        return new ErrorMessage(httpStatus, LocalDateTime.now(), message, errors);
    }

}
