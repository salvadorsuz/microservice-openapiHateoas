package com.challenge.microservicechallenge.service.converter;

import com.challenge.microservicechallenge.service.common.Converter;
import com.challenge.microservicechallenge.service.model.Transaction;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Component
public class TransactionToMoneyMovementConverter implements Converter<Transaction, MoneyMovement> {

    @Override
    public MoneyMovement convert(Transaction in) {
        MoneyMovement out = null;
        if(nonNull(in)) {
            return MoneyMovement.builder()
                    .reference(defaultIfNull(in.getReference(), UUID.randomUUID().toString()))
                    .iban(in.getAccountIban())
                    .description(in.getDescription())
                    .date(defaultIfNull(in.getDate(), new Date()))
                    .amount(in.getAmount())
                    .fee(in.getFee())
                    .build();
        }
        return out;
    }
}
