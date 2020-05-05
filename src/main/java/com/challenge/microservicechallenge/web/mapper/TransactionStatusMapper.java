package com.challenge.microservicechallenge.web.mapper;

import com.challenge.microservicechallenge.service.model.TransactionStatus;
import com.challenge.microservicechallenge.web.model.TransactionStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionStatusMapper {

    TransactionStatusMapper INSTANCE = Mappers.getMapper(TransactionStatusMapper.class);

    TransactionStatusDto transactionStatusToTransactionStatusDto(TransactionStatus transactionStatus);

    TransactionStatus transactionStatusDtoToTransactionStatus(TransactionStatusDto transactionDto);
}
