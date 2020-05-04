package com.challenge.microservicechallenge.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("AmountFeeCalculator tests ")
class AmountFeeCalculatorTest {

    @Test
    @DisplayName("Calculate with positive Amount and positive Fee ")
    public void givenPositiveAmountAndPositiveFeeThenReturnValidCalculation() {
        //given
        Double amount = 10D;
        Double fee = 1D;
        AmountFeeCalculator calculator = AmountFeeCalculator.valueOf(amount,fee);

        //when
        Double out = calculator.calculate();

        //then
        assertEquals((amount-fee), out);
    }

    @Test
    @DisplayName("Calculate with positive Amount and Null Fee ")
    public void givenPositiveAmountAndNullFeeThenReturnValidCalculation() {
        //given
        Double amount = 10D;
        Double fee = null;
        AmountFeeCalculator calculator = AmountFeeCalculator.valueOf(amount,null);
        //when
        Double out = calculator.calculate();

        //then
        assertEquals(amount, out);
    }

}