package com.challenge.microservicechallenge.common;

import com.challenge.microservicechallenge.exception.ValidationException;

@FunctionalInterface
public interface Validator<T> {

   void validate(T t) throws ValidationException;
}
