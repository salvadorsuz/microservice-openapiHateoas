package com.challenge.microservicechallenge.web;

import com.challenge.microservicechallenge.exception.ConflictException;
import com.challenge.microservicechallenge.service.api.CreateTransactionService;
import com.challenge.microservicechallenge.service.api.SearchTransactionService;
import com.challenge.microservicechallenge.web.hateoas.TransactionModelAssembler;
import com.challenge.microservicechallenge.web.hateoas.TransactionStatusModelAssembler;
import com.challenge.microservicechallenge.web.mapper.TransactionMapper;
import com.challenge.microservicechallenge.web.model.TransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/transactions")
@Tag(name = "Transactions", description = "the Transactions API")
public class TransactionController {

    private final CreateTransactionService createTransactionService;
    private final SearchTransactionService searchTransactionService;
    private final TransactionModelAssembler transactionModelAssembler;

    @Autowired
    public TransactionController(CreateTransactionService createTransactionService,
                                 SearchTransactionService searchTransactionService,
                                 TransactionModelAssembler transactionModelAssembler,
                                 TransactionStatusModelAssembler transactionStatusModelAssembler) {
        this.createTransactionService = createTransactionService;
        this.searchTransactionService = searchTransactionService;
        this.transactionModelAssembler = transactionModelAssembler;
    }

    @PostMapping
    @Operation(summary = "Create transaction", description = "Create a transaction")
    @ApiResponses({
            @ApiResponse(responseCode  = "201", description  = "Successful creation of a transaction"),
            @ApiResponse(responseCode  = "400", description  = "Transaction not valid"),
            @ApiResponse(responseCode  = "409", description  = "Conflict in transaction creation")
    })
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<TransactionDto>> create(@Valid @RequestBody TransactionDto in) {
        return Stream.of(in)
                .map(TransactionMapper.INSTANCE::transactionDtoToTransaction)
                .map(createTransactionService::create)
                .map(TransactionMapper.INSTANCE::transactionToTransactionDto)
                .map(transactionModelAssembler::toModel)
                .map(entityModel -> ResponseEntity
                        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel))
                .findFirst()
                .orElseThrow(() -> new ConflictException("Error in Transaction creation"));
    }

    @GetMapping
    @Operation(summary = "Search transaction", description = "Returns transactions by Iban. Optional ordered by amount")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of transactions"),
            @ApiResponse(responseCode = "204", description = "No transactions")
    })
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CollectionModel<EntityModel<TransactionDto>>> search(@RequestParam(value="accountIban") Optional<String> accountIban,
                                                                               @RequestParam(value="orderAmountAsc") Optional<Boolean> orderAmountAsc) {
        List<TransactionDto> dtos = searchTransactionService.search(accountIban, orderAmountAsc)
                .stream()
                .map(TransactionMapper.INSTANCE::transactionToTransactionDto)
                .collect(Collectors.toList());

        ResponseEntity<CollectionModel<EntityModel<TransactionDto>>> response = ResponseEntity.noContent().build();
        if(!dtos.isEmpty()) {
            CollectionModel<EntityModel<TransactionDto>> entities = transactionModelAssembler.toCollectionModel(dtos);
            response = ResponseEntity.ok().body(entities);
        }
        return response;
    }

}
