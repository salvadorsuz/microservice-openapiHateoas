package com.challenge.microservicechallenge.service.api;

import com.challenge.microservicechallenge.service.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface SearchTransactionService {

    List<Transaction> search(Optional<String> accountIban, Optional<Boolean> orderAmountAsc);
}
