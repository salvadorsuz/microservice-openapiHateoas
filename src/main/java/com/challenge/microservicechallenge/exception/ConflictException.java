package com.challenge.microservicechallenge.exception;

import java.util.List;

public class ConflictException extends ApiException{

    public ConflictException(String msg) {
        super(msg);
    }

    public ConflictException(Throwable ex) {
        super(ex);
    }

    public ConflictException(String msg, Throwable ex) {
        super(msg, ex);
    }

    public ConflictException(List<String> errors) {
        super(errors);
    }

    public ConflictException(List<String> errors, Throwable ex) {
        super(errors, ex);
    }
}
