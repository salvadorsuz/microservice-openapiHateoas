package com.challenge.microservicechallenge.web;

import com.challenge.microservicechallenge.model.Channels;
import com.challenge.microservicechallenge.model.Transaction;
import com.challenge.microservicechallenge.model.TransactionStatus;
import com.challenge.microservicechallenge.service.create.CreateTransactionService;
import com.challenge.microservicechallenge.service.search.SearchTransactionService;
import com.challenge.microservicechallenge.service.status.TransactionStatusService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    private final CreateTransactionService createTransactionService;
    private final SearchTransactionService searchTransactionService;
    private final TransactionStatusService transactionStatusService;

    @Autowired
    public TransactionController(CreateTransactionService createTransactionService,
                                 SearchTransactionService searchTransactionService,
                                 TransactionStatusService transactionStatusService) {
        this.createTransactionService = createTransactionService;
        this.searchTransactionService = searchTransactionService;
        this.transactionStatusService = transactionStatusService;
    }

    @PostMapping
    @ApiOperation(value = "Create transaction", notes = "Create a transaction")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successful creation of a transaction"),
            @ApiResponse(code = 400, message = "Transaction not valid"),
            @ApiResponse(code = 409, message = "Conflict in transaction creation")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@Valid @RequestBody Transaction in) {

        return createTransactionService.create(in);
    }

    @GetMapping
    @ApiOperation(value = "Search transactions", notes = "Returns transactions by Iban. Optional ordered by amount")
    @ApiResponses({
            @ApiResponse(code = 200, message = "List of transactions")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> search(@RequestParam(value="accountIban") Optional<String> accountIban,
                                    @RequestParam(value="orderAmountAsc") Optional<Boolean> orderAmountAsc) {
        return searchTransactionService.search(accountIban, orderAmountAsc);
    }

    @GetMapping("/{reference}")
    @ApiOperation(value = "Get one transaction by reference and channel",
            notes = "Returns one transaction status by reference. Optional channel: CLIENT, ATM, INTERNAL. Default value CLIENT")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Exists this transaction")
    })
    @ResponseStatus(HttpStatus.OK)
    public TransactionStatus status(@PathVariable String reference, @RequestParam(value="channel")  Optional<Channels> channel) {
        return transactionStatusService.getStatus(reference, channel);
    }

}
