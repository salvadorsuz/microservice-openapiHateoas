package com.challenge.microservicechallenge.web;

import com.challenge.microservicechallenge.exception.ConflictException;
import com.challenge.microservicechallenge.exception.ValidationException;
import com.challenge.microservicechallenge.model.Channels;
import com.challenge.microservicechallenge.model.Status;
import com.challenge.microservicechallenge.model.Transaction;
import com.challenge.microservicechallenge.model.TransactionStatus;
import com.challenge.microservicechallenge.service.create.CreateTransactionService;
import com.challenge.microservicechallenge.service.search.SearchTransactionService;
import com.challenge.microservicechallenge.service.status.TransactionStatusService;
import com.challenge.microservicechallenge.utils.TestUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("TransactionController tests ")
class TransactionControllerTest {

    private final String IBAN = "123";
    private final Double AMOUNT = 10D;
    private final String REFERENCE = "AAA-111";

    private MockMvc mockMvc;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private CreateTransactionService createTransactionService;

    @Mock
    private SearchTransactionService searchTransactionService;

    @Mock
    private TransactionStatusService transactionStatusService;

    private Transaction transaction;

    @BeforeEach
    public void init() {

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(transactionController)
                .setControllerAdvice(new CustomizedResponseEntityExceptionHandler())
                .build();

        transaction = Transaction.builder().accountIban(IBAN).amount(AMOUNT).reference(REFERENCE).build();
    }

    @Nested
    @DisplayName("Test plan Create Transaction")
    class WhenCreateTransaction {

        @Test
        @DisplayName("Create valid transaction ")
        public void givenValidTransactionThenCreate() throws Exception  {
            //given
            when(createTransactionService.create(any(Transaction.class))).thenReturn(transaction);

            //when
            final ResultActions result = mockMvc.perform(
                    post("/transactions/").contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(transaction))
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(createTransactionService, times(1)).create(any(Transaction.class));

            result.andExpect(status().isCreated());
            result.andExpect(jsonPath("$.accountIban").value(transaction.getAccountIban()));
            result.andExpect(jsonPath("$.reference").value(transaction.getReference()));
            result.andExpect(jsonPath("$.amount").value(transaction.getAmount()));
        }

        @Test
        @DisplayName("Create invalid transaction then Bad Request")
        public void givenInvalidTransactionThenBadRequest() throws Exception  {
            //given
            List<String> errors = Collections.singletonList("Invalid");
            Mockito.when(createTransactionService.create(any(Transaction.class))).thenThrow(new ValidationException(errors));

            //when
            final ResultActions result = mockMvc.perform(
                    post("/transactions/").contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(transaction))
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(createTransactionService, times(1)).create(any(Transaction.class));

            result.andExpect(status().isBadRequest());

        }

        @Test
        @DisplayName("Error in create transaction then Conflict")
        public void givenTransactionThenConflict() throws Exception  {
            //given
            Mockito.when(createTransactionService.create(any(Transaction.class))).thenThrow(new ConflictException("Conflict"));

            //when
            final ResultActions result = mockMvc.perform(
                    post("/transactions/").contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(transaction))
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(createTransactionService, times(1)).create(any(Transaction.class));

            result.andExpect(status().isConflict());

        }

    }


    @Nested
    @DisplayName("Test plan Search Transaction")
    class WhenSearchTransaction {

        @Test
        @DisplayName("Search transaction without iban and order")
        public void givenNoIbanNoOrderThenSearchAll() throws Exception {
            //given
            List<Transaction> transactions = Collections.singletonList(transaction);
            when(searchTransactionService.search(Optional.empty(), Optional.empty())).thenReturn(transactions);

            //when
            final ResultActions result = mockMvc.perform(
                    get("/transactions/")
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(searchTransactionService, times(1)).search(Optional.empty(), Optional.empty());

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.length()").value(transactions.size()));
            result.andExpect(jsonPath("$[*].accountIban", containsInAnyOrder(transaction.getAccountIban())));
            result.andExpect(jsonPath("$[*].reference", containsInAnyOrder(transaction.getReference())));
            result.andExpect(jsonPath("$[*].amount", containsInAnyOrder(transaction.getAmount())));
        }

        @Test
        @DisplayName("Search transaction with iban and order")
        public void givenIbanOrderThenSearch() throws Exception {
            //given
            List<Transaction> transactions = Collections.singletonList(transaction);
            when(searchTransactionService.search(Optional.of(IBAN), Optional.of(Boolean.TRUE))).thenReturn(transactions);

            //when
            final ResultActions result = mockMvc.perform(
                    get("/transactions")
                            .param("accountIban", IBAN)
                            .param("orderAmountAsc", "true")
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(searchTransactionService, times(1)).search(Optional.of(IBAN), Optional.of(Boolean.TRUE));

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.length()").value(transactions.size()));
            result.andExpect(jsonPath("$[*].accountIban", containsInAnyOrder(transaction.getAccountIban())));
            result.andExpect(jsonPath("$[*].reference", containsInAnyOrder(transaction.getReference())));
            result.andExpect(jsonPath("$[*].amount", containsInAnyOrder(transaction.getAmount())));
        }


    }

    @Nested
    @DisplayName("Test plan Status Transaction")
    class WhenStatusTransaction {

        @Test
        @DisplayName("Status transaction with valid reference and NO channel")
        public void givenNoChannelThenReturnValidStatus() throws Exception {
            //given
            TransactionStatus transactionStatus =
                    TransactionStatus.builder().reference(REFERENCE).status(Status.PENDING).build();

            when(transactionStatusService.getStatus(REFERENCE, Optional.empty() )).thenReturn(transactionStatus);

            //when
            final ResultActions result = mockMvc.perform(
                    get("/transactions/{reference}", REFERENCE)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(transactionStatusService, times(1)).getStatus(REFERENCE, Optional.empty());

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.reference").value(REFERENCE));
            result.andExpect(jsonPath("$.status").value(Status.PENDING.toString()));
        }

        @Test
        @DisplayName("Status transaction with valid reference and CLIENT channel")
        public void givenValidChannelThenReturnValidStatus() throws Exception {
            //given
            TransactionStatus transactionStatus =
                    TransactionStatus.builder().reference(REFERENCE).status(Status.PENDING).build();

            when(transactionStatusService.getStatus(REFERENCE, Optional.of(Channels.CLIENT))).thenReturn(transactionStatus);

            //when
            final ResultActions result = mockMvc.perform(
                    get("/transactions/{reference}", REFERENCE)
                            .param("channel", Channels.CLIENT.toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(transactionStatusService, times(1)).getStatus(REFERENCE, Optional.of(Channels.CLIENT));

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.reference").value(REFERENCE));
            result.andExpect(jsonPath("$.status").value(Status.PENDING.toString()));
        }

    }

}