package com.example.gitapi.config;

import com.example.gitapi.exceptions.ExceededGitApiRateLimit;
import com.example.gitapi.exceptions.UsernameNotFoundException;
import com.example.gitapi.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Internal server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<Object> handleCurrencyNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), ex.getStatusCode());
        return new ResponseEntity<>(error,  ex.getStatusCode());
    }

    @ExceptionHandler(ExceededGitApiRateLimit.class)
    public final ResponseEntity<Object> handleCurrencyRatesProcessingException(ExceededGitApiRateLimit ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), ex.getStatusCode());
        return new ResponseEntity<>(error, ex.getStatusCode());
    }


}
