package com.challenge.microservicechallenge.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@ApiModel(description="All details about the transaction")
public class Transaction {

    public Transaction() {
    }

    public Transaction(String reference, @NotNull(message = "IBAN cannot be null") String accountIban, @FutureOrPresent(message = "Transaction date must be future or present") Date date, @NotNull(message = "Amount cannot be null") Double amount, @PositiveOrZero(message = "Fee must be positive or Zero") Double fee, String description) {
        this.reference = reference;
        this.accountIban = accountIban;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.description = description;
    }

    @ApiModelProperty(notes="The transaction unique reference number in our system.")
    private String reference;

    @NotNull(message = "IBAN cannot be null")
    @ApiModelProperty(notes="The IBAN number of the account where the transaction has happened. Mandatory")
    private String accountIban;

    @ApiModelProperty(notes="Date when the transaction took place.")
    private Date date;

    @NotNull(message = "Amount cannot be null")
    @ApiModelProperty(notes="Amount of the transaction. Mandatory. Can be positive or negative")
    private Double amount;

    @PositiveOrZero(message = "Fee must be positive or Zero")
    @ApiModelProperty(notes="Fee that will be deducted from the amount. If present then must be positive or Zero")
    private Double fee;

    @ApiModelProperty(notes="The description of the transaction\n")
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

    public static Transaction.TransactionBuilder builder() {
        return new Transaction.TransactionBuilder();
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

        public Transaction.TransactionBuilder reference(final String reference) {
            this.reference = reference;
            return this;
        }

        public Transaction.TransactionBuilder accountIban(final String accountIban) {
            this.accountIban = accountIban;
            return this;
        }

        public Transaction.TransactionBuilder date(final Date date) {
            this.date = date;
            return this;
        }

        public Transaction.TransactionBuilder amount(final Double amount) {
            this.amount = amount;
            return this;
        }

        public Transaction.TransactionBuilder fee(final Double fee) {
            this.fee = fee;
            return this;
        }

        public Transaction.TransactionBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public Transaction build() {
            return new Transaction(reference, accountIban, date, amount, fee, description);
        }

    }
}
