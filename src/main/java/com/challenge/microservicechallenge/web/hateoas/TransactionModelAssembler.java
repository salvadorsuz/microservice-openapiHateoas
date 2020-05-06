package com.challenge.microservicechallenge.web.hateoas;

import com.challenge.microservicechallenge.web.TransactionController;
import com.challenge.microservicechallenge.web.TransactionStatusController;
import com.challenge.microservicechallenge.web.model.TransactionDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransactionModelAssembler implements RepresentationModelAssembler<TransactionDto, EntityModel<TransactionDto>> {

    public static final String REL_STATUS = "status";
    public static final String REL_TRANSACTIONS = "transactions";

    @Override
    public EntityModel<TransactionDto> toModel(TransactionDto transaction) {
        return new EntityModel<>(transaction,
                linkTo(methodOn(TransactionStatusController.class).status(transaction.getReference(), null))
                        .withSelfRel(),
                linkTo(methodOn(TransactionStatusController.class).status(transaction.getReference(), null))
                        .withRel(REL_STATUS),
                linkTo(methodOn(TransactionController.class).search(Optional.of(transaction.getAccountIban()), Optional.empty()))
                        .withRel(REL_TRANSACTIONS));
    }

    @Override
    public CollectionModel<EntityModel<TransactionDto>> toCollectionModel(Iterable<? extends TransactionDto> transactions) {

        List<EntityModel<TransactionDto>> transactionsEntities = StreamSupport.stream(transactions.spliterator(), false)
                .map(x -> toModel(x))
                .collect(Collectors.toList());

        Optional<String> iban = getIbanRel(transactionsEntities);

        return new CollectionModel<>(transactionsEntities,
                linkTo(methodOn(TransactionController.class).search(iban, Optional.empty())).withSelfRel());
    }

    private Optional<String> getIbanRel( List<EntityModel<TransactionDto>> transactionsEntities) {
        Set<String> ibans = transactionsEntities.stream().map(x -> x.getContent().getAccountIban()).collect(Collectors.toSet());
        if(ibans.size() == 1){
            return Optional.ofNullable(ibans.iterator().next());
        }
        return Optional.empty();
    }
}
