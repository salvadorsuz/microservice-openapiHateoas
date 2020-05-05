package com.challenge.microservicechallenge.web.hateoas;

import com.challenge.microservicechallenge.web.model.TransactionStatusDto;
import com.challenge.microservicechallenge.web.TransactionStatusController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransactionStatusModelAssembler implements RepresentationModelAssembler<TransactionStatusDto, EntityModel<TransactionStatusDto>> {
    @Override
    public EntityModel<TransactionStatusDto> toModel(TransactionStatusDto entity) {
        return new EntityModel<>(entity,
                linkTo(methodOn(TransactionStatusController.class).status(entity.getReference(), Optional.empty()))
                        .withSelfRel());
    }
}
