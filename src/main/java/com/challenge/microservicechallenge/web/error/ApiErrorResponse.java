package com.challenge.microservicechallenge.web.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ApiErrorResponse {
    private HttpStatus status;
    private LocalDateTime errorDate;
    private List<String> errors;


    ApiErrorResponse(final HttpStatus status, final LocalDateTime errorDate, final List<String> errors) {
        this.status = status;
        this.errorDate = errorDate;
        this.errors = errors;
    }

    public HttpStatus getStatus() {
        return this.status;
    }


    public LocalDateTime getErrorDate() {
        return this.errorDate;
    }


    public List<String> getErrors() {
        return this.errors;
    }


    public void setStatus(final HttpStatus status) {
        this.status = status;
    }


    public void setErrorDate(final LocalDateTime errorDate) {
        this.errorDate = errorDate;
    }


    public void setErrors(final List<String> errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiErrorResponse that = (ApiErrorResponse) o;
        return status == that.status &&
                Objects.equals(errorDate, that.errorDate) &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, errorDate, errors);
    }

    public static ApiErrorResponse.ApiErrorResponseBuilder builder() {
        return new ApiErrorResponse.ApiErrorResponseBuilder();
    }

    public static class ApiErrorResponseBuilder {

        private HttpStatus status;

        private LocalDateTime errorDate;

        private List<String> errors;


        ApiErrorResponseBuilder() {
        }

        public ApiErrorResponse.ApiErrorResponseBuilder status(final HttpStatus status) {
            this.status = status;
            return this;
        }

        public ApiErrorResponse.ApiErrorResponseBuilder errorDate(final LocalDateTime errorDate) {
            this.errorDate = errorDate;
            return this;
        }

        public ApiErrorResponse.ApiErrorResponseBuilder errors(final List<String> errors) {
            this.errors = errors;
            return this;
        }

        public ApiErrorResponse build() {
            return new ApiErrorResponse(this.status, this.errorDate, this.errors);
        }

    }
}
