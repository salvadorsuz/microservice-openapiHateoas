package com.challenge.microservicechallenge.service.converter;

import com.challenge.microservicechallenge.service.common.Converter;

import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import com.challenge.microservicechallenge.service.model.TransactionStatus;

public abstract class MoneyMovementToTransactionStatusConverter implements Converter<MoneyMovement, TransactionStatus> {

    protected DateToLocalDateConverter dateToLocalDateConverter;

    public MoneyMovementToTransactionStatusConverter(DateToLocalDateConverter dateToLocalDateConverter) {
        this.dateToLocalDateConverter = dateToLocalDateConverter;
    }

}
