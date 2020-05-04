package com.challenge.microservicechallenge.service.search;

import com.challenge.microservicechallenge.model.Transaction;
import com.challenge.microservicechallenge.model.converter.MoneyMovementToTransactionConverter;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import com.challenge.microservicechallenge.persistence.respository.MoneyMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("SearchTransactionService tests ")
class SearchTransactionServiceTest {

    private final String IBAN= "123";
    private final Double AMOUNT= 10D;
    private final String REFERENCE= "AAA-111";

    @InjectMocks
    private SearchTransactionService searchTransactionService;

    @Mock
    private MoneyMovementRepository repository;

    @Mock
    private MoneyMovementToTransactionConverter moneyMovementToTransactionConverter;

    private MoneyMovement moneyMovement;

    private Transaction transaction;

    @BeforeEach
    public void init() {
        transaction = Transaction.builder().reference(REFERENCE).amount(AMOUNT).accountIban(IBAN).build();
        moneyMovement = MoneyMovement.builder().reference(REFERENCE).amount(AMOUNT).iban(IBAN).build();
    }

    @Nested
    @DisplayName("Test plan Search Transactions")
    class WhenExistsTransaction {

        @Test
        @DisplayName("List all transactions ")
        public void givenNoParamsThenListAll() {
            //given
            when(repository.findAll(any(Example.class))).thenReturn(Collections.singletonList(moneyMovement));
            when(moneyMovementToTransactionConverter.convert(moneyMovement)).thenReturn(transaction);

            //when
            List<Transaction> result = searchTransactionService.search( Optional.empty(), Optional.empty());
            assertNotNull(result);

            //then
            verify(repository, times(1)).findAll(any(Example.class));
            verify(moneyMovementToTransactionConverter, times(1)).convert(moneyMovement);

            assertAll(
                    "result",
                    () -> assertNotNull(result),
                    () -> assertFalse(result.isEmpty()),
                    () -> assertIterableEquals(Collections.singleton(transaction), result)
            );

        }

        @Test
        @DisplayName("Find transactions by persisted iban")
        public void givenPersistedIbanThenTransactionsByIban() {
            //given
            when(repository.findAll(any(Example.class))).thenReturn(Collections.singletonList(moneyMovement));
            when(moneyMovementToTransactionConverter.convert(moneyMovement)).thenReturn(transaction);

            //when
            List<Transaction> result = searchTransactionService.search(Optional.of(IBAN), Optional.empty());

            //then
            verify(repository, times(1)).findAll(any(Example.class));
            verify(moneyMovementToTransactionConverter, times(1)).convert(moneyMovement);

            assertAll(
                    "result",
                    () -> assertNotNull(result),
                    () -> assertFalse(result.isEmpty()),
                    () -> assertIterableEquals(Collections.singleton(transaction), result)
            );
        }


        @Test
        @DisplayName("Find transactions by persisted iban and order amount ASC")
        public void givenPersistedIbanAndOrderAscThenTransactionsByIbanOrderedAsc() {
            //given
            when(repository.findAll(any(Example.class), any(Sort.class))).thenReturn(Collections.singletonList(moneyMovement));
            when(moneyMovementToTransactionConverter.convert(moneyMovement)).thenReturn(transaction);

            //when
            List<Transaction> result = searchTransactionService.search(Optional.of(IBAN), Optional.of(Boolean.TRUE));

            //then
            verify(repository, times(1)).findAll(any(Example.class), any(Sort.class));
            verify(moneyMovementToTransactionConverter, times(1)).convert(moneyMovement);

            assertAll(
                    "result",
                    () -> assertNotNull(result),
                    () -> assertFalse(result.isEmpty()),
                    () -> assertIterableEquals(Collections.singleton(transaction), result)
            );
        }

    }

    @Nested
    @DisplayName("Test plan Search Transactions empty")
    class WhenNoTransaction {
        @Test
        @DisplayName("No transactions by no persisted iban")
        public void givenNoPersistedIbanThenNoTransactions() {
            //given
            when(repository.findAll(any(Example.class), any(Sort.class))).thenReturn(Collections.EMPTY_LIST);

            //when
            List<Transaction> result = searchTransactionService.search(Optional.of(IBAN), Optional.of(Boolean.TRUE));

            //then
            verify(repository, times(1)).findAll(any(Example.class), any(Sort.class));

            assertTrue(result.isEmpty());
        }

    }

}