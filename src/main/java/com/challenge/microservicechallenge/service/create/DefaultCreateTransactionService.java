package com.challenge.microservicechallenge.service.create;

import com.challenge.microservicechallenge.service.common.Validator;
import com.challenge.microservicechallenge.exception.ConflictException;
import com.challenge.microservicechallenge.exception.ValidationException;
import com.challenge.microservicechallenge.persistence.respository.MoneyMovementRepository;
import com.challenge.microservicechallenge.service.api.CreateTransactionService;
import com.challenge.microservicechallenge.service.converter.MoneyMovementToTransactionConverter;
import com.challenge.microservicechallenge.service.converter.TransactionToMoneyMovementConverter;
import com.challenge.microservicechallenge.service.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class DefaultCreateTransactionService implements CreateTransactionService {

    private final MoneyMovementRepository repository;

    private final TransactionToMoneyMovementConverter transactionToMoneyMovementConverter;

    private final MoneyMovementToTransactionConverter moneyMovementToTransactionConverter;

    private final Validator<Transaction> validator;

    @Autowired
    public DefaultCreateTransactionService(MoneyMovementRepository repository,
                                           TransactionToMoneyMovementConverter transactionToMoneyMovementConverter,
                                           MoneyMovementToTransactionConverter moneyMovementToTransactionConverter,
                                           Validator<Transaction> validator) {
        this.repository = repository;
        this.transactionToMoneyMovementConverter = transactionToMoneyMovementConverter;
        this.moneyMovementToTransactionConverter = moneyMovementToTransactionConverter;
        this.validator = validator;
    }

    @Override
    public Transaction create(Transaction in) throws ConflictException, ValidationException {
        validator.validate(in);

        return Stream.of(in)
                .map(transactionToMoneyMovementConverter::convert)
                .map(repository::save)
                .map(moneyMovementToTransactionConverter::convert)
                .findFirst().orElseThrow(() -> new ConflictException("Error in Transaction creation"));
    }

}
