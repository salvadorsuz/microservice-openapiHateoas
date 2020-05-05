package com.challenge.microservicechallenge.service.converter;

import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import com.challenge.microservicechallenge.service.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("TransactionToMoneyMovementConverter tests ")
class TransactionToMoneyMovementConverterTest {

    private static final String IBAN ="AAA";
    private static final Double AMOUNT = 10D;
    private static final Double FEE = 1D;
    private static final String DESCRIPTION ="DESC";
    private static final String REFERENCE ="123";
    private static final Date DATE = new Date();

    @Autowired
    private TransactionToMoneyMovementConverter converter;

    @Test
    @DisplayName("Given null transaction ")
    public void givenNullTransactionThenNullMoneyMovement() {
        //when
        MoneyMovement result = converter.convert(null);

        //then
        assertNull(result);
    }

    @Test
    @DisplayName("Given valid transaction empty reference and date")
    public void givenValidTransactionThenConvert() {
        //given
        Transaction in = Transaction.builder().accountIban(IBAN).amount(AMOUNT).fee(FEE).description(DESCRIPTION).build();

        //when
        MoneyMovement result = converter.convert(in);

        //then
        assertAll(
                "result",
                () -> assertNotNull(result),
                () -> assertNotNull(result.getReference()),
                () -> assertNotNull(result.getDate()),
                () -> assertEquals(AMOUNT, result.getAmount()),
                () -> assertEquals(FEE, result.getFee()),
                () -> assertEquals(DESCRIPTION, result.getDescription())
        );
    }

    @Test
    @DisplayName("Given valid transaction with reference and date")
    public void givenValidTransactionReferenceDateThenConvert() {
        //given
        Transaction in = Transaction.builder().reference(REFERENCE).date(DATE)
                .accountIban(IBAN).amount(AMOUNT).fee(FEE).description(DESCRIPTION).build();

        //when
        MoneyMovement result = converter.convert(in);

        //then
        assertAll(
                "result",
                () -> assertNotNull(result),
                () -> assertEquals(REFERENCE, result.getReference()),
                () -> assertEquals(DATE, result.getDate()),
                () -> assertEquals(AMOUNT, result.getAmount()),
                () -> assertEquals(FEE, result.getFee()),
                () -> assertEquals(DESCRIPTION, result.getDescription())
        );
    }


}