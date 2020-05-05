package com.challenge.microservicechallenge.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@Schema(description = "All details about the transaction")
public class TransactionDto {

    public TransactionDto() {
    }

    public TransactionDto(String reference, @NotNull(message = "IBAN cannot be null") String accountIban, Date date, @NotNull(message = "Amount cannot be null") Double amount, @PositiveOrZero(message = "Fee must be positive or Zero") Double fee, String description) {
        this.reference = reference;
        this.accountIban = accountIban;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.description = description;
    }

    @Schema(description = "The transaction unique reference number in our system", example = "123-A")
    private String reference;

    @NotNull(message = "IBAN cannot be null")
    @Schema(description = "The IBAN number of the account where the transaction has happened. Mandatory", example = "123-456-789")
    private String accountIban;

    @Schema(description = "Date when the transaction took place.")
    private Date date;

    @NotNull(message = "Amount cannot be null")
    @Schema(description = "Amount of the transaction. Mandatory. Can be positive or negative")
    private Double amount;

    @PositiveOrZero(message = "Fee must be positive or Zero")
    @Schema(description = "Fee that will be deducted from the amount. If present then must be positive or Zero")
    private Double fee;

    @Schema(description = "The description of the transaction")
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

    public static TransactionDto.TransactionBuilder builder() {
        return new TransactionDto.TransactionBuilder();
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

        public TransactionDto.TransactionBuilder reference(final String reference) {
            this.reference = reference;
            return this;
        }

        public TransactionDto.TransactionBuilder accountIban(final String accountIban) {
            this.accountIban = accountIban;
            return this;
        }

        public TransactionDto.TransactionBuilder date(final Date date) {
            this.date = date;
            return this;
        }

        public TransactionDto.TransactionBuilder amount(final Double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionDto.TransactionBuilder fee(final Double fee) {
            this.fee = fee;
            return this;
        }

        public TransactionDto.TransactionBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public TransactionDto build() {
            return new TransactionDto(reference, accountIban, date, amount, fee, description);
        }

    }
}
