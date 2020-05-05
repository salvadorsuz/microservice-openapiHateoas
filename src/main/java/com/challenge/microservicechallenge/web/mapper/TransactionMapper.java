package com.challenge.microservicechallenge.web.mapper;

import com.challenge.microservicechallenge.service.model.Transaction;
import com.challenge.microservicechallenge.web.model.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionDto transactionToTransactionDto(Transaction transaction);

    Transaction transactionDtoToTransaction(TransactionDto transactionDto);
}
