package com.challenge.microservicechallenge.exception;

import java.util.List;

public class NotFoundException extends ApiException{

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(Throwable ex) {
        super(ex);
    }

    public NotFoundException(String msg, Throwable ex) {
        super(msg, ex);
    }

    public NotFoundException(List<String> errors) {
        super(errors);
    }

    public NotFoundException(List<String> errors, Throwable ex) {
        super(errors, ex);
    }

}
