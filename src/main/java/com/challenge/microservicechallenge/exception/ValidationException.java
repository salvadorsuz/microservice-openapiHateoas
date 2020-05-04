package com.challenge.microservicechallenge.exception;

import java.util.List;

public class ValidationException extends ApiException{

    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(Throwable ex) {
        super(ex);
    }

    public ValidationException(String msg, Throwable ex) {
        super(msg, ex);
    }

    public ValidationException(List<String> errors) {
        super(errors);
    }

    public ValidationException(List<String> errors, Throwable ex) {
        super(errors, ex);
    }
}
