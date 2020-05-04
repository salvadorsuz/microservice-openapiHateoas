package com.challenge.microservicechallenge.exception;

import java.util.List;

public class ApiException extends RuntimeException {
    private static final long serialVersionUID = -3131376188048232311L;
    private final List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public ApiException(String msg) {
        super(msg);
        this.errors = null;
    }

    public ApiException(Throwable ex) {
        super(ex);
        this.errors = null;
    }

    public ApiException(String msg, Throwable ex) {
        super(msg, ex);
        this.errors = null;
    }

    public ApiException(List<String> errors) {
        this.errors = errors;
    }

    public ApiException(List<String> errors, Throwable ex) {
        super(ex);
        this.errors = errors;
    }
}
