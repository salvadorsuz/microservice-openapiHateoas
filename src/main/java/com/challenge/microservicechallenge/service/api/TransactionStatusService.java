package com.challenge.microservicechallenge.service.api;

import com.challenge.microservicechallenge.service.model.Channels;
import com.challenge.microservicechallenge.service.model.TransactionStatus;


import java.util.Optional;

public interface TransactionStatusService {

    TransactionStatus getStatus(String reference, Optional<Channels> channel);
}
