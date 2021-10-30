package pl.java.homebudget.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.java.homebudget.exception.*;
import pl.java.homebudget.exception.dto.ErrorMessage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(InvalidDateFormatException.class)
    protected ResponseEntity<ErrorMessage> handleInvalidDateFormatException(InvalidDateFormatException ex) {
        ErrorMessage errorMessage = getErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage(), Collections.emptyList());

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingExpenseFilterSettingException.class)
    protected ResponseEntity<ErrorMessage> handleMissingExpenseFilterSettingException(MissingExpenseFilterSettingException ex) {
        ErrorMessage errorMessage = getErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage(), Collections.emptyList());

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage errorMessage = getErrorMessage(status, ex.getMessage(), Collections.emptyList());

        return new ResponseEntity<>(errorMessage, headers, status);
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
