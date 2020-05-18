package com.challenge.microservicechallenge.service.create;

import com.challenge.microservicechallenge.exception.ValidationException;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import com.challenge.microservicechallenge.persistence.respository.MoneyMovementRepository;
import com.challenge.microservicechallenge.service.converter.MoneyMovementToTransactionConverter;
import com.challenge.microservicechallenge.service.converter.TransactionToMoneyMovementConverter;
import com.challenge.microservicechallenge.service.model.Transaction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


//@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateTransactionService tests ")
class CreateTransactionServiceTest {

    private final String IBAN= "123";
    private final Double AMOUNT= 10D;
    private final String REFERENCE= "AAA-111";

    @InjectMocks
    private DefaultCreateTransactionService service;

    @Mock
    private MoneyMovementRepository repository;

    @Mock
    private TransactionToMoneyMovementConverter transactionToMoneyMovementConverter;

    @Mock
    private MoneyMovementToTransactionConverter moneyMovementToTransactionConverter;

    @Mock
    private TransactionValidator validator;

    private Transaction transactionInsert ;

    @BeforeEach
    public void init() {
        transactionInsert = Transaction.builder().accountIban(IBAN).amount(AMOUNT).build();
    }

    @Nested
    @DisplayName("Test plan Create valid Transaction")
    class WhenTransactionIsValid {

        @Test
        @DisplayName("Create valid transaction without reference and date")
        public void givenValidTransactionThenCreate() {
            //given
            Date date = new Date();
            Long id = 1L;

            Transaction transactionDB = Transaction.builder().accountIban(IBAN).amount(AMOUNT)
                    .reference(REFERENCE).date(date).build();

            MoneyMovement moneyMovementInsert = MoneyMovement.builder().iban(IBAN)
                    .amount(AMOUNT).reference(REFERENCE).date(date).build();
            MoneyMovement moneyMovementDB = MoneyMovement.builder().iban(IBAN)
                    .amount(AMOUNT).reference(REFERENCE).date(date).id(id).build();

            doNothing().when(validator).validate(transactionInsert);
            when(transactionToMoneyMovementConverter.convert(transactionInsert)).thenReturn(moneyMovementInsert);
            when(repository.save(moneyMovementInsert)).thenReturn(moneyMovementDB);
            when(moneyMovementToTransactionConverter.convert(moneyMovementDB)).thenReturn(transactionDB);

            //when
            Transaction result = service.create(transactionInsert);

            //then
            verify(validator, times(1)).validate(any(Transaction.class));
            verify(transactionToMoneyMovementConverter, times(1)).convert(any(Transaction.class));
            verify(repository, times(1)).save(any(MoneyMovement.class));
            verify(moneyMovementToTransactionConverter, times(1)).convert(any(MoneyMovement.class));

            assertAll(
                    "result",
                    () -> assertNotNull(result),
                    () -> assertNotNull(result.getAccountIban()),
                    () -> assertNotNull(result.getDate()),
                    () -> assertNotNull(result.getReference())
            );
        }

    }


    @Nested
    @DisplayName("Test plan Create Invalid Transaction")
    class WhenTransactionIsInvalid {

        @Test
        @DisplayName("Create invalid transaction ")
        public void givenInvalidTransactionThenThrowValidationException() {
            //given
            List<String> errors = Collections.singletonList("Invalid");
            Mockito.doThrow(new ValidationException(errors))
                    .when(validator).validate(transactionInsert);

            //when
            ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> service.create(transactionInsert));

            //then
            assertIterableEquals(errors, exception.getErrors());

            verify(validator, times(1)).validate(any(Transaction.class));
            verifyNoInteractions(transactionToMoneyMovementConverter);
            verifyNoInteractions(repository);
            verifyNoInteractions(moneyMovementToTransactionConverter);
        }

    }
}