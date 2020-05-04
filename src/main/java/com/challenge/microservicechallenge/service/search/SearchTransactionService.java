package com.challenge.microservicechallenge.service.search;

import com.challenge.microservicechallenge.model.converter.MoneyMovementToTransactionConverter;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import com.challenge.microservicechallenge.persistence.respository.MoneyMovementRepository;
import com.challenge.microservicechallenge.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchTransactionService {

    private final MoneyMovementRepository repository;

    private final MoneyMovementToTransactionConverter moneyMovementToTransactionConverter;

    @Autowired
    public SearchTransactionService(MoneyMovementRepository repository, MoneyMovementToTransactionConverter moneyMovementToTransactionConverter) {
        this.repository = repository;
        this.moneyMovementToTransactionConverter = moneyMovementToTransactionConverter;
    }

    public List<Transaction> search(Optional<String> accountIban, Optional<Boolean> orderAmountAsc) {
        List<MoneyMovement> moneyMovements =  findMoneyMovements(accountIban, orderAmountAsc);

        return moneyMovements.stream()
                .map(moneyMovementToTransactionConverter::convert)
                .collect(Collectors.toList());
    }

    private List<MoneyMovement> findMoneyMovements(Optional<String> accountIban, Optional<Boolean> orderAmountAsc) {
        Example<MoneyMovement> example = Example.of(MoneyMovement.builder().iban(accountIban.orElse(null)).build());
        Optional<Sort> amountSort = getSortAmount(orderAmountAsc);
        return amountSort.map(orders -> repository.findAll(example, orders)).orElseGet(() -> repository.findAll(example));
    }

    private Optional<Sort> getSortAmount(Optional<Boolean> orderAmountAsc) {
        return orderAmountAsc.map(aBoolean -> aBoolean ? Sort.by("amount").ascending() : Sort.by("amount").descending());
    }
}
