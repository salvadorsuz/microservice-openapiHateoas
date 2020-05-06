package com.challenge.microservicechallenge.web;

import com.challenge.microservicechallenge.service.model.Channels;
import com.challenge.microservicechallenge.service.model.Status;
import com.challenge.microservicechallenge.service.model.TransactionStatus;
import com.challenge.microservicechallenge.web.model.ChannelsDto;
import com.challenge.microservicechallenge.web.model.StatusDto;
import com.challenge.microservicechallenge.web.model.TransactionStatusDto;
import com.challenge.microservicechallenge.service.status.DefaultTransactionStatusService;
import com.challenge.microservicechallenge.web.hateoas.TransactionStatusModelAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("TransactionStatusController tests ")
class TransactionStatusControllerTest {

    private final String REFERENCE = "AAA-111";

    private MockMvc mockMvc;

    @InjectMocks
    private TransactionStatusController controller;

    @Mock
    private DefaultTransactionStatusService transactionStatusService;

    @Mock
    private TransactionStatusModelAssembler transactionStatusModelAssembler;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new CustomizedResponseEntityExceptionHandler())
                .build();

    }

    @Nested
    @DisplayName("Test plan Status Transaction")
    class WhenStatusTransaction {

        @Test
        @DisplayName("Status transaction with valid reference and NO channel")
        public void givenNoChannelThenReturnValidStatus() throws Exception {
            //given
            TransactionStatusDto transactionStatusDto =
                    TransactionStatusDto.builder().reference(REFERENCE).status(StatusDto.PENDING).build();

            TransactionStatus transactionStatus =
                    TransactionStatus.builder().reference(REFERENCE).status(Status.PENDING).build();

            EntityModel<TransactionStatusDto> transactionStatusEntityModel =  new EntityModel<>(transactionStatusDto,
                    linkTo(methodOn(TransactionStatusController.class).status(transactionStatusDto.getReference(), null))
                            .withSelfRel());

            when(transactionStatusService.getStatus(REFERENCE, Optional.empty() )).thenReturn(transactionStatus);
            when(transactionStatusModelAssembler.toModel(any(TransactionStatusDto.class))).thenReturn(transactionStatusEntityModel);

            //when
            final ResultActions result = mockMvc.perform(
                    get("/transactions/{reference}/status", REFERENCE)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(transactionStatusService, times(1)).getStatus(REFERENCE, Optional.empty());

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.reference").value(REFERENCE));
            result.andExpect(jsonPath("$.status").value(StatusDto.PENDING.toString()));

            result.andExpect(jsonPath("$.links").isNotEmpty());
            result.andExpect(jsonPath("$.links[*].rel", containsInAnyOrder(IanaLinkRelations.SELF.toString())));
        }

        @Test
        @DisplayName("Status transaction with valid reference and CLIENT channel")
        public void givenValidChannelThenReturnValidStatus() throws Exception {
            //given
            TransactionStatusDto transactionStatusDto =
                    TransactionStatusDto.builder().reference(REFERENCE).status(StatusDto.PENDING).build();

            TransactionStatus transactionStatus =
                    TransactionStatus.builder().reference(REFERENCE).status(Status.PENDING).build();

            EntityModel<TransactionStatusDto> transactionStatusEntityModel =  new EntityModel<>(transactionStatusDto,
                    linkTo(methodOn(TransactionStatusController.class).status(transactionStatusDto.getReference(), null))
                            .withSelfRel());
            when(transactionStatusService.getStatus(REFERENCE, Optional.of(Channels.CLIENT))).thenReturn(transactionStatus);
            when(transactionStatusModelAssembler.toModel(any(TransactionStatusDto.class))).thenReturn(transactionStatusEntityModel);

            //when
            final ResultActions result = mockMvc.perform(
                    get("/transactions/{reference}/status", REFERENCE)
                            .param("channel", ChannelsDto.CLIENT.toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(transactionStatusService, times(1)).getStatus(REFERENCE, Optional.of(Channels.CLIENT));

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.reference").value(REFERENCE));
            result.andExpect(jsonPath("$.status").value(StatusDto.PENDING.toString()));

            result.andExpect(jsonPath("$.links").isNotEmpty());
            result.andExpect(jsonPath("$.links[*].rel", containsInAnyOrder(IanaLinkRelations.SELF.toString())));
        }

    }
}