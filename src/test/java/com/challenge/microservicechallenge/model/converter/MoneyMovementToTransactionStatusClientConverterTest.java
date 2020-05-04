package com.challenge.microservicechallenge.model.converter;

import com.challenge.microservicechallenge.model.Status;
import com.challenge.microservicechallenge.model.TransactionStatus;
import com.challenge.microservicechallenge.persistence.entitiy.MoneyMovement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("MoneyMovementToTransactionStatusClientConverter tests ")
class MoneyMovementToTransactionStatusClientConverterTest {

    private static final String REFERENCE ="AAA";
    private static final Double AMOUNT = 10D;
    private static final Double FEE = 1D;

    @InjectMocks
    private MoneyMovementToTransactionStatusClientConverter converter;

    @Mock
    private DateToLocalDateConverter dateToLocalDateConverter;

    private MoneyMovement moneyMovement;

    @BeforeEach
    public void init() {
        moneyMovement = MoneyMovement.builder().reference(REFERENCE).amount(AMOUNT).fee(FEE).build();
    }


    @Test
    @DisplayName("Given date before ")
    public void givenDateBeforeThenSettled() {
        //given
        LocalDate localDate = LocalDate.now().minusDays(1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        moneyMovement.setDate(date);

        when(dateToLocalDateConverter.convert(any(Date.class))).thenReturn(localDate);

        //when
        TransactionStatus result = converter.convert(moneyMovement);

        //then
        assertAll(
                "result",
                () -> assertNotNull(result),
                () -> assertEquals(REFERENCE, result.getReference()),
                () -> assertEquals(Status.SETTLED, result.getStatus()),
                () -> assertEquals((AMOUNT-FEE), result.getAmount()),
                () -> assertNull(result.getFee())
        );

        verify(dateToLocalDateConverter, times(1)).convert(any(Date.class));

    }

    @Test
    @DisplayName("Given date today ")
    public void givenDateTodayThenPending() {
        //given
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        moneyMovement.setDate(date);

        when(dateToLocalDateConverter.convert(any(Date.class))).thenReturn(localDate);

        //when
        TransactionStatus result = converter.convert(moneyMovement);

        //then
        assertAll(
                "result",
                () -> assertNotNull(result),
                () -> assertEquals(REFERENCE, result.getReference()),
                () -> assertEquals(Status.PENDING, result.getStatus()),
                () -> assertEquals((AMOUNT-FEE), result.getAmount()),
                () -> assertNull(result.getFee())
        );

        verify(dateToLocalDateConverter, times(1)).convert(any(Date.class));
    }

    @Test
    @DisplayName("Given date future ")
    public void givenDateFutureThenFuture() {
        //given
        LocalDate localDate = LocalDate.now().plusDays(1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        moneyMovement.setDate(date);

        when(dateToLocalDateConverter.convert(any(Date.class))).thenReturn(localDate);

        //when
        TransactionStatus result = converter.convert(moneyMovement);

        //then
        assertAll(
                "result",
                () -> assertNotNull(result),
                () -> assertEquals(REFERENCE, result.getReference()),
                () -> assertEquals(Status.FUTURE, result.getStatus()),
                () -> assertEquals((AMOUNT-FEE), result.getAmount()),
                () -> assertNull(result.getFee())
        );

        verify(dateToLocalDateConverter, times(1)).convert(any(Date.class));
    }

}