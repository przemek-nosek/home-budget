package pl.java.homebudget.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.java.homebudget.exception.AssetNotFoundException;
import pl.java.homebudget.exception.dto.ErrorMessage;

import java.time.LocalDateTime;
import java.util.Collections;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AssetNotFoundException.class)
    public <T extends RuntimeException> ResponseEntity<ErrorMessage> handleNotFoundException(T ex) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage(), Collections.emptyList());

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
