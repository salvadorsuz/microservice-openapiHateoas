package com.challenge.microservicechallenge.web;

import com.challenge.microservicechallenge.exception.ApiException;
import com.challenge.microservicechallenge.exception.ConflictException;
import com.challenge.microservicechallenge.exception.NotFoundException;
import com.challenge.microservicechallenge.exception.ValidationException;
import com.challenge.microservicechallenge.web.error.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        return this.handleException(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({NotFoundException.class})
    //@ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return this.handleApiException(ex, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ConflictException.class})
    //@ResponseStatus(HttpStatus.CONFLICT)
    public final ResponseEntity<Object> handleConflictException(ConflictException ex, WebRequest request) {
        return this.handleApiException(ex, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({ValidationException.class})
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        return this.handleApiException(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<Object> handleApiException(ApiException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Optional.ofNullable(ex.getErrors()).orElse(Collections.singletonList(ex.getMessage()));
        ApiErrorResponse exceptionResponse = ApiErrorResponse.builder().status(status).errorDate(LocalDateTime.now()).errors(errors).build();
        return this.handleExceptionInternal(ex, exceptionResponse, headers, status, request);
    }

    private ResponseEntity<Object> handleException(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        ApiErrorResponse exceptionResponse = ApiErrorResponse.builder().status(status).errorDate(LocalDateTime.now()).errors(errors).build();
        return this.handleExceptionInternal(ex, exceptionResponse, headers, status, request);
    }

}
