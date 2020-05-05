package com.challenge.microservicechallenge.service.common;

@FunctionalInterface
public interface Converter<T,U> {
    U convert(T t);
}
