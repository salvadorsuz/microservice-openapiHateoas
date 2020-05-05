package com.challenge.microservicechallenge.service.converter;

import com.challenge.microservicechallenge.service.common.AmountFeeCalculator;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import com.challenge.microservicechallenge.service.model.Status;
import com.challenge.microservicechallenge.service.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("MoneyMovementToTransactionStatusATMConverter")
public class MoneyMovementToTransactionStatusATMConverter extends MoneyMovementToTransactionStatusConverter{

    @Autowired
    public MoneyMovementToTransactionStatusATMConverter(DateToLocalDateConverter dateToLocalDateConverter) {
        super(dateToLocalDateConverter);
    }

    @Override
    public TransactionStatus convert(MoneyMovement moneyMovement) {
        Status status = getStatus(moneyMovement);
        Double amount = AmountFeeCalculator.valueOf(moneyMovement.getAmount(), moneyMovement.getFee()).calculate();
        return TransactionStatus.builder()
                .reference(moneyMovement.getReference())
                .status(status)
                .amount(amount).build();
    }

    private Status getStatus(MoneyMovement moneyMovement) {
        LocalDate today = LocalDate.now();
        LocalDate transactionDate = dateToLocalDateConverter.convert(moneyMovement.getDate());

        if(transactionDate.isBefore(today)) {
            return Status.SETTLED;
        }
        return Status.PENDING;
    }
}
