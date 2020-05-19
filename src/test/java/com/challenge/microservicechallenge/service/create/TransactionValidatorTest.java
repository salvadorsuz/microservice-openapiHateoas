package com.challenge.microservicechallenge.service.create;

import com.challenge.microservicechallenge.exception.ValidationException;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import com.challenge.microservicechallenge.persistence.respository.MoneyMovementRepository;
import com.challenge.microservicechallenge.service.model.Transaction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;


//@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionValidator tests ")
class TransactionValidatorTest {

    private final String IBAN= "123";
    private final Double AMOUNT= 10D;
    private final String REFERENCE= "AAA-111";

    @InjectMocks
    private TransactionValidator transactionValidator;

    @Mock
    private MoneyMovementRepository repository;

    private Transaction transaction;

    private MoneyMovement moneyMovement;

    @BeforeEach
    public void init() {
        transaction = Transaction.builder().accountIban(IBAN).amount(AMOUNT).build();
        moneyMovement = MoneyMovement.builder().reference(REFERENCE).amount(AMOUNT).iban(IBAN).build();
    }

    @Nested
    @DisplayName("Test plan Validate valid Transaction")
    class WhenTransactionIsValid {

        @Test
        @DisplayName("Amount valid and reference null ")
        public void givenValidAmountAndReferenceNullThenValidate() {
            //given
            when(repository.findByIban(IBAN)).thenReturn(Collections.EMPTY_LIST);

            //when
            transactionValidator.validate(transaction);

            //then
            verify(repository, times(1)).findByIban(anyString());
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("Valid Amount  and valid reference  ")
        public void givenValidAmountAndValidReferenceThenValidate() {
            //given
            transaction.setReference(REFERENCE);

            when(repository.findByReference(REFERENCE)).thenReturn(Collections.EMPTY_LIST);
            when(repository.findByIban(IBAN)).thenReturn(Collections.EMPTY_LIST);

            //when
            transactionValidator.validate(transaction);

            //then
            verify(repository, times(1)).findByReference(anyString());
            verify(repository, times(1)).findByIban(anyString());
        }

        @Test
        @DisplayName("Negative Amount valid and reference null ")
        public void givenNegativeValidAmountAndReferenceNullThenValidate() {
            //given
            transaction.setAmount(-AMOUNT);

            when(repository.findByIban(IBAN)).thenReturn(Collections.singletonList(moneyMovement));

            //when
            transactionValidator.validate(transaction);

            //then
            verify(repository, times(1)).findByIban(anyString());
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("Test plan Validate invalid Transaction ")
    class WhenTransactionIsInvalid {

        @Test
        @DisplayName("Repeated reference")
        public void givenRepeatedReferenceThenValidationException() {
            //given
            transaction.setReference(REFERENCE);

            when(repository.findByReference(REFERENCE)).thenReturn(Collections.singletonList(moneyMovement));
            when(repository.findByIban(IBAN)).thenReturn(Collections.EMPTY_LIST);

            //when
            ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> transactionValidator.validate(transaction));

            //then
            List<String> errorsExpected = Collections.singletonList("Exists a transaction with the same reference");
            assertIterableEquals(errorsExpected, exception.getErrors());

            verify(repository, times(1)).findByReference(anyString());
            verify(repository, times(1)).findByIban(anyString());
        }

        @Test
        @DisplayName("Negative balance")
        public void givenNegativeBalanceThenValidationException() {
            //given
            transaction.setAmount(-AMOUNT);
            moneyMovement.setAmount(AMOUNT-1);

            when(repository.findByIban(IBAN)).thenReturn(Collections.singletonList(moneyMovement));

            //when
            ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> transactionValidator.validate(transaction));

            //then
            List<String> errorsExpected = Collections.singletonList("Amount exceeds the balance");
            assertIterableEquals(errorsExpected, exception.getErrors());

            verify(repository, times(1)).findByIban(anyString());
            verifyNoMoreInteractions(repository);
        }

        @Test
        @DisplayName("Negative balance and repeated reference")
        public void givenRepeatedRefrenceAndNegativeBalanceThenValidationException() {
            //given
            transaction.setReference(REFERENCE);
            transaction.setAmount(-AMOUNT);
            moneyMovement.setAmount(AMOUNT-1);

            when(repository.findByReference(REFERENCE)).thenReturn(Collections.singletonList(moneyMovement));
            when(repository.findByIban(IBAN)).thenReturn(Collections.singletonList(moneyMovement));

            //when
            ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> transactionValidator.validate(transaction));

            //then
            List<String> errorsExpected = Arrays.asList(
                    "Exists a transaction with the same reference",
                    "Amount exceeds the balance");
            assertIterableEquals(errorsExpected, exception.getErrors());

            verify(repository, times(1)).findByReference(anyString());
            verify(repository, times(1)).findByIban(anyString());

        }
    }

}