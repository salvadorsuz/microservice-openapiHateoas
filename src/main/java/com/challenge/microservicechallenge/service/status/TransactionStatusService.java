package com.challenge.microservicechallenge.service.status;

import com.challenge.microservicechallenge.model.Channels;
import com.challenge.microservicechallenge.model.Status;
import com.challenge.microservicechallenge.model.TransactionStatus;
import com.challenge.microservicechallenge.model.converter.MoneyMovementToTransactionStatusATMConverter;
import com.challenge.microservicechallenge.model.converter.MoneyMovementToTransactionStatusClientConverter;
import com.challenge.microservicechallenge.model.converter.MoneyMovementToTransactionStatusConverter;
import com.challenge.microservicechallenge.model.converter.MoneyMovementToTransactionStatusInternalConverter;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import com.challenge.microservicechallenge.persistence.respository.MoneyMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Default CHANNEL is CLIENT
 */

@Service
public class TransactionStatusService {

    private final MoneyMovementRepository repository;

    private final MoneyMovementToTransactionStatusClientConverter moneyMovementToTransactionStatusClientConverter;

    private final MoneyMovementToTransactionStatusATMConverter moneyMovementToTransactionStatusATMConverter;

    private final MoneyMovementToTransactionStatusInternalConverter moneyMovementToTransactionStatusInternalConverter;

    private static final Channels DEFAULT_CHANNEL = Channels.CLIENT;

    @Autowired
    public TransactionStatusService(MoneyMovementRepository repository,
                                    MoneyMovementToTransactionStatusClientConverter moneyMovementToTransactionStatusClientConverter,
                                    MoneyMovementToTransactionStatusATMConverter moneyMovementToTransactionStatusATMConverter,
                                    MoneyMovementToTransactionStatusInternalConverter moneyMovementToTransactionStatusInternalConverter) {
        this.repository = repository;
        this.moneyMovementToTransactionStatusClientConverter = moneyMovementToTransactionStatusClientConverter;
        this.moneyMovementToTransactionStatusATMConverter = moneyMovementToTransactionStatusATMConverter;
        this.moneyMovementToTransactionStatusInternalConverter = moneyMovementToTransactionStatusInternalConverter;
    }

    private MoneyMovementToTransactionStatusConverter getConverter(Optional<Channels> channel) {
        Channels ch = channel.orElse(DEFAULT_CHANNEL);

        if(ch.equals(Channels.CLIENT))
            return moneyMovementToTransactionStatusClientConverter;
        if(ch.equals(Channels.ATM))
            return moneyMovementToTransactionStatusATMConverter;
        if(ch.equals(Channels.INTERNAL))
            return moneyMovementToTransactionStatusInternalConverter;
        return moneyMovementToTransactionStatusClientConverter;
    }

    private TransactionStatus mapByChannel(MoneyMovement moneyMovement, Optional<Channels> channel) {
        MoneyMovementToTransactionStatusConverter converter = getConverter(channel);
        return converter.convert(moneyMovement);
    }

    public TransactionStatus getStatus(String reference, Optional<Channels> channel) {
        return repository.findByReference(reference).stream()
                .findFirst()
                .map(moneyMovement -> this.mapByChannel(moneyMovement, channel))
                .orElseGet(() -> TransactionStatus.builder().reference(reference).status(Status.INVALID).build());
    }

}
