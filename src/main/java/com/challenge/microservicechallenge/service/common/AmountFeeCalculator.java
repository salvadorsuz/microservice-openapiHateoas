package com.challenge.microservicechallenge.service.common;

import java.util.Optional;

public class AmountFeeCalculator {

    private Double amount;
    private Double fee;

    private AmountFeeCalculator(Double amount, Double fee) {
        this.amount = amount;
        this.fee = fee;
    }

    public static AmountFeeCalculator valueOf(Double amount, Double fee) {
        return new AmountFeeCalculator(amount, fee);
    }

    public Double calculate() {
        Double feeApplied = Optional.ofNullable(fee).orElse(0D);
        return amount-feeApplied;
    }
}
