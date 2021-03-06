package com.challenge.microservicechallenge.service.converter;

import com.challenge.microservicechallenge.service.common.Converter;
import com.challenge.microservicechallenge.service.model.Transaction;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class MoneyMovementToTransactionConverter implements Converter<MoneyMovement, Transaction> {

    @Override
    public Transaction convert(MoneyMovement in) {
        Transaction out = null;
        if(nonNull(in)){
            out = Transaction.builder()
                    .reference(in.getReference())
                    .accountIban(in.getIban())
                    .date(in.getDate())
                    .amount(in.getAmount())
                    .fee(in.getFee())
                    .description(in.getDescription())
                    .build();
        }
        return out;
    }
}
