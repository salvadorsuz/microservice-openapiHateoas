package com.challenge.microservicechallenge.service.create;

import com.challenge.microservicechallenge.exception.ConflictException;
import com.challenge.microservicechallenge.exception.ValidationException;
import com.challenge.microservicechallenge.model.converter.MoneyMovementToTransactionConverter;
import com.challenge.microservicechallenge.model.converter.TransactionToMoneyMovementConverter;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import com.challenge.microservicechallenge.persistence.respository.MoneyMovementRepository;
import com.challenge.microservicechallenge.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class CreateTransactionService {

    private final MoneyMovementRepository repository;

    private final TransactionToMoneyMovementConverter transactionToMoneyMovementConverter;

    private final MoneyMovementToTransactionConverter moneyMovementToTransactionConverter;

    private final TransactionValidator validator;

    @Autowired
    public CreateTransactionService(MoneyMovementRepository repository,
                                    TransactionToMoneyMovementConverter transactionToMoneyMovementConverter,
                                    MoneyMovementToTransactionConverter moneyMovementToTransactionConverter,
                                    TransactionValidator validator) {
        this.repository = repository;
        this.transactionToMoneyMovementConverter = transactionToMoneyMovementConverter;
        this.moneyMovementToTransactionConverter = moneyMovementToTransactionConverter;
        this.validator = validator;
    }

    public Transaction create(Transaction in) throws ConflictException, ValidationException {
        validator.validate(in);

        return Stream.of(in)
                .map(transactionToMoneyMovementConverter::convert)
                .map(repository::save)
                .map(moneyMovementToTransactionConverter::convert)
                .findFirst().orElseThrow(() -> new ConflictException("Error in Transaction creation"));
    }

}
