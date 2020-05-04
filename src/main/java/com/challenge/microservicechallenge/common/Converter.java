package com.challenge.microservicechallenge.common;

@FunctionalInterface
public interface Converter<T,U> {
    U convert(T t);
}
