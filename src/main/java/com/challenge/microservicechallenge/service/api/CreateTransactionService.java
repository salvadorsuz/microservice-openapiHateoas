package com.challenge.microservicechallenge.service.api;

import com.challenge.microservicechallenge.exception.ConflictException;
import com.challenge.microservicechallenge.exception.ValidationException;
import com.challenge.microservicechallenge.service.model.Transaction;

public interface CreateTransactionService {

    Transaction create(Transaction in) throws ConflictException, ValidationException;
}
