package com.challenge.microservicechallenge.model.converter;

import com.challenge.microservicechallenge.model.Channels;
import com.challenge.microservicechallenge.model.Status;
import com.challenge.microservicechallenge.model.TransactionStatus;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("MoneyMovementToTransactionStatusInternalConverter")
public class MoneyMovementToTransactionStatusInternalConverter extends MoneyMovementToTransactionStatusConverter{

    @Autowired
    public MoneyMovementToTransactionStatusInternalConverter(DateToLocalDateConverter dateToLocalDateConverter) {
        super(dateToLocalDateConverter);
    }

    @Override
    public TransactionStatus convert(MoneyMovement moneyMovement) {
        Status status = getStatus(moneyMovement);
        return TransactionStatus.builder()
                .reference(moneyMovement.getReference())
                .status(status)
                .amount(moneyMovement.getAmount())
                .fee(moneyMovement.getFee())
                .build();
    }

    private Status getStatus(MoneyMovement moneyMovement) {
        LocalDate today = LocalDate.now();
        LocalDate transactionDate = dateToLocalDateConverter.convert(moneyMovement.getDate());

        if(transactionDate.isBefore(today)) {
            return Status.SETTLED;
        }
        if(transactionDate.isAfter(today)){
            return Status.FUTURE;
        }
        return Status.PENDING;
    }
}
