package com.challenge.microservicechallenge.service.create;

import com.challenge.microservicechallenge.exception.ValidationException;
import com.challenge.microservicechallenge.persistence.respository.MoneyMovementRepository;
import com.challenge.microservicechallenge.common.AmountFeeCalculator;
import com.challenge.microservicechallenge.common.Validator;
import com.challenge.microservicechallenge.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TransactionValidator implements Validator<Transaction> {
    private final MoneyMovementRepository repository;

    @Autowired
    public TransactionValidator(MoneyMovementRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(Transaction transaction) throws ValidationException {
        final List<String> errors = new ArrayList<>();
        validateNotRepeatedReference(transaction).ifPresent(errors::add);
        validateEnoughMoney(transaction).ifPresent(errors::add);

        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private Optional<String> validateNotRepeatedReference(Transaction transaction) {
        return Optional.ofNullable(transaction.getReference())
                .map(repository::findByReference)
                .filter(x -> !x.isEmpty())
                .map(x -> "Exists a transaction with the same reference");
    }

    private Optional<String> validateEnoughMoney(Transaction transaction) {
        Double transactionAmount = AmountFeeCalculator.valueOf(transaction.getAmount(), transaction.getFee()).calculate();

        Double balance = repository.findByIban(transaction.getAccountIban()).stream()
                .map(m -> AmountFeeCalculator.valueOf(m.getAmount(), m.getFee()).calculate())
                .reduce(transactionAmount, Double::sum);

        return balance < 0 ? Optional.of("Amount exceeds the balance") : Optional.empty();
    }

}
