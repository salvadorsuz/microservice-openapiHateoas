package com.challenge.microservicechallenge.web;

import com.challenge.microservicechallenge.exception.ConflictException;
import com.challenge.microservicechallenge.service.api.TransactionStatusService;
import com.challenge.microservicechallenge.service.model.Channels;
import com.challenge.microservicechallenge.service.model.TransactionStatus;
import com.challenge.microservicechallenge.web.hateoas.TransactionStatusModelAssembler;
import com.challenge.microservicechallenge.web.mapper.ChannelsMapper;
import com.challenge.microservicechallenge.web.mapper.TransactionStatusMapper;
import com.challenge.microservicechallenge.web.model.ChannelsDto;
import com.challenge.microservicechallenge.web.model.TransactionStatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/transactions/{reference}")
@Tag(name = "Transactions Status", description = "the Transactions status API")
public class TransactionStatusController {

    private final TransactionStatusService transactionStatusService;
    private final TransactionStatusModelAssembler transactionStatusModelAssembler;

    @Autowired
    public TransactionStatusController(TransactionStatusService transactionStatusService,
                                       TransactionStatusModelAssembler transactionStatusModelAssembler) {

        this.transactionStatusService = transactionStatusService;
        this.transactionStatusModelAssembler = transactionStatusModelAssembler;
    }

    @GetMapping("/status")
    @Operation(summary = "Get one transaction by reference and channel",
            description = "Returns one transaction status by reference. Optional channel: CLIENT, ATM, INTERNAL. Default value CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exists this transaction"),
            @ApiResponse(responseCode = "204", description = "No transactions")
    })
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EntityModel<TransactionStatusDto>> status(@PathVariable("reference") String reference,
                                                                    @RequestParam(value="channel", required=false)ChannelsDto channel) {

        Optional<Channels> ch = Optional.ofNullable(channel).map(ChannelsMapper.INSTANCE::channelsDtoToChannels);
        TransactionStatus transactionStatus =  transactionStatusService.getStatus(reference, ch);

        return Stream.of(transactionStatus)
                .map(TransactionStatusMapper.INSTANCE::transactionStatusToTransactionStatusDto)
                .map(transactionStatusModelAssembler::toModel)
                .map(em -> ResponseEntity.ok().body(em))
                .findFirst()
                .orElseThrow(() -> new ConflictException("Error in Transaction status"));
    }

}
