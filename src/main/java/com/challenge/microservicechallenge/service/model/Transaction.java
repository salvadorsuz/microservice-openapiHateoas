package com.challenge.microservicechallenge.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

public class Transaction {

    public Transaction() {
    }

    public Transaction(String reference, @NotNull(message = "IBAN cannot be null") String accountIban, Date date, @NotNull(message = "Amount cannot be null") Double amount, @PositiveOrZero(message = "Fee must be positive or Zero") Double fee, String description) {
        this.reference = reference;
        this.accountIban = accountIban;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.description = description;
    }

    private String reference;

    @NotNull(message = "IBAN cannot be null")
    private String accountIban;

    private Date date;

    @NotNull(message = "Amount cannot be null")
    private Double amount;

    @PositiveOrZero(message = "Fee must be positive or Zero")
    private Double fee;

    private String description;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAccountIban() {
        return accountIban;
    }

    public void setAccountIban(String accountIban) {
        this.accountIban = accountIban;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    public static class TransactionBuilder {

        private String reference;

        private String accountIban;

        private Date date;

        private Double amount;

        private Double fee;

        private String description;

        TransactionBuilder() {
        }

        public TransactionBuilder reference(final String reference) {
            this.reference = reference;
            return this;
        }

        public TransactionBuilder accountIban(final String accountIban) {
            this.accountIban = accountIban;
            return this;
        }

        public TransactionBuilder date(final Date date) {
            this.date = date;
            return this;
        }

        public TransactionBuilder amount(final Double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder fee(final Double fee) {
            this.fee = fee;
            return this;
        }

        public TransactionBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public Transaction build() {
            return new Transaction(reference, accountIban, date, amount, fee, description);
        }

    }
}
