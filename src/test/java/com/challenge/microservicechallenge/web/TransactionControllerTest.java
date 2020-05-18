package com.challenge.microservicechallenge.web;

import com.challenge.microservicechallenge.exception.ConflictException;
import com.challenge.microservicechallenge.exception.ValidationException;
import com.challenge.microservicechallenge.service.model.Transaction;
import com.challenge.microservicechallenge.web.model.TransactionDto;
import com.challenge.microservicechallenge.service.create.DefaultCreateTransactionService;
import com.challenge.microservicechallenge.service.search.DefaultSearchTransactionService;
import com.challenge.microservicechallenge.utils.TestUtils;
import com.challenge.microservicechallenge.web.hateoas.TransactionModelAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
@DisplayName("TransactionController tests ")
class TransactionControllerTest {

    private final String IBAN = "123";
    private final Double AMOUNT = 10D;
    private final String REFERENCE = "AAA-111";

    private MockMvc mockMvc;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private DefaultCreateTransactionService createTransactionService;

    @Mock
    private DefaultSearchTransactionService searchTransactionService;

    @Mock
    private TransactionModelAssembler transactionModelAssembler;

    private TransactionDto transactionDto;

    private Transaction transaction;

    private EntityModel<TransactionDto> transactionEntityModel;

    private CollectionModel<EntityModel<TransactionDto>> transactionsEntityModel;

    @BeforeEach
    public void init() {

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(transactionController)
                .setControllerAdvice(new CustomizedResponseEntityExceptionHandler())
                .build();

        transactionDto = TransactionDto.builder().accountIban(IBAN).amount(AMOUNT).reference(REFERENCE).build();

        transaction = Transaction.builder().accountIban(IBAN).amount(AMOUNT).reference(REFERENCE).build();

        transactionEntityModel =  new EntityModel<>(transactionDto,
                linkTo(methodOn(TransactionStatusController.class).status(transaction.getReference(), null))
                    .withSelfRel());

        transactionsEntityModel = new CollectionModel<>(Collections.singleton(transactionEntityModel),
                linkTo(methodOn(TransactionController.class).search(Optional.ofNullable(IBAN), Optional.empty())).withSelfRel());
    }

    @Nested
    @DisplayName("Test plan Create Transaction")
    class WhenCreateTransaction {

        @Test
        @DisplayName("Create valid transaction ")
        public void givenValidTransactionThenCreate() throws Exception  {
            //given
            when(createTransactionService.create(any(Transaction.class))).thenReturn(transaction);
            when(transactionModelAssembler.toModel(any(TransactionDto.class))).thenReturn(transactionEntityModel);

            //when
            final ResultActions result = mockMvc.perform(
                    post("/transactions/").contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(transaction))
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(createTransactionService, times(1)).create(any(Transaction.class));

            result.andExpect(status().isCreated());
            result.andExpect(jsonPath("$.accountIban").value(transactionDto.getAccountIban()));
            result.andExpect(jsonPath("$.reference").value(transactionDto.getReference()));
            result.andExpect(jsonPath("$.amount").value(transactionDto.getAmount()));

            result.andExpect(header().exists("location"));
            result.andExpect(header().string("location", containsString("transactions/"+REFERENCE+"/status")));

            result.andExpect(jsonPath("$.links").isNotEmpty());
            result.andExpect(jsonPath("$.links[*].rel", containsInAnyOrder(IanaLinkRelations.SELF.toString())));

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
            when(transactionModelAssembler.toCollectionModel(any(Iterable.class))).thenReturn(transactionsEntityModel);

            //when
            final ResultActions result = mockMvc.perform(
                    get("/transactions/")
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(searchTransactionService, times(1)).search(Optional.empty(), Optional.empty());

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.content.length()").value(transactions.size()));
            result.andExpect(jsonPath("$.content[*].accountIban", containsInAnyOrder(transactionDto.getAccountIban())));
            result.andExpect(jsonPath("$.content[*].reference", containsInAnyOrder(transactionDto.getReference())));
            result.andExpect(jsonPath("$.content[*].amount", containsInAnyOrder(transactionDto.getAmount())));

            result.andExpect(jsonPath("$.links").isNotEmpty());
            result.andExpect(jsonPath("$.links[*].rel", containsInAnyOrder(IanaLinkRelations.SELF.toString())));

        }

        @Test
        @DisplayName("Search transaction with iban and order")
        public void givenIbanOrderThenSearch() throws Exception {
            //given
            List<Transaction> transactions = Collections.singletonList(transaction);
            when(searchTransactionService.search(Optional.of(IBAN), Optional.of(Boolean.TRUE))).thenReturn(transactions);
            when(transactionModelAssembler.toCollectionModel(any(Iterable.class))).thenReturn(transactionsEntityModel);

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
            result.andExpect(jsonPath("$.content.length()").value(transactions.size()));
            result.andExpect(jsonPath("$.content[*].accountIban", containsInAnyOrder(transactionDto.getAccountIban())));
            result.andExpect(jsonPath("$.content[*].reference", containsInAnyOrder(transactionDto.getReference())));
            result.andExpect(jsonPath("$.content[*].amount", containsInAnyOrder(transactionDto.getAmount())));

            result.andExpect(jsonPath("$.links").isNotEmpty());
            result.andExpect(jsonPath("$.links[*].rel", containsInAnyOrder(IanaLinkRelations.SELF.toString())));
        }

        @Test
        @DisplayName("Search NO transaction with iban and order")
        public void givenIbanOrderThenNoTransactions() throws Exception {
            //given
            when(searchTransactionService.search(Optional.of(IBAN), Optional.of(Boolean.TRUE))).thenReturn(Collections.EMPTY_LIST);

            //when
            final ResultActions result = mockMvc.perform(
                    get("/transactions")
                            .param("accountIban", IBAN)
                            .param("orderAmountAsc", "true")
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            verify(searchTransactionService, times(1)).search(Optional.of(IBAN), Optional.of(Boolean.TRUE));

            result.andExpect(status().isNoContent());
        }
    }

}