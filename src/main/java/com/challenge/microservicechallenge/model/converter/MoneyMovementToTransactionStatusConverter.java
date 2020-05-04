package com.challenge.microservicechallenge.model.converter;

import com.challenge.microservicechallenge.common.Converter;
import com.challenge.microservicechallenge.model.TransactionStatus;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;

public abstract class MoneyMovementToTransactionStatusConverter implements Converter<MoneyMovement, TransactionStatus> {

    protected DateToLocalDateConverter dateToLocalDateConverter;

    public MoneyMovementToTransactionStatusConverter(DateToLocalDateConverter dateToLocalDateConverter) {
        this.dateToLocalDateConverter = dateToLocalDateConverter;
    }

}
