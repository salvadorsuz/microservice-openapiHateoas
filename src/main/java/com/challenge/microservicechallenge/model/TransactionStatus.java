package com.challenge.microservicechallenge.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="All details about the transaction status")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionStatus {

    @ApiModelProperty(notes="The transaction unique reference number in our system.")
    private String reference;

    @ApiModelProperty(notes="Status of the transaction. Can be: PENDING, SETTLED, INVALID, FUTURE")
    private Status status;

    @ApiModelProperty(notes="Amount of the transaction. Mandatory. Can be positive or negative")
    private Double amount;

    @ApiModelProperty(notes="Fee that will be deducted from the amount. If present then must be positive or Zero")
    private Double fee;

    public TransactionStatus() {
    }

    public TransactionStatus(String reference, Status status, Double amount, Double fee) {
        this.reference = reference;
        this.status = status;
        this.amount = amount;
        this.fee = fee;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public static TransactionStatus.TransactionStatusBuilder builder() {
        return new TransactionStatus.TransactionStatusBuilder();
    }

    public static class TransactionStatusBuilder {

        private String reference;

        private Status status;

        private Double amount;

        private Double fee;

        TransactionStatusBuilder() {
        }

        public TransactionStatus.TransactionStatusBuilder reference(final String reference) {
            this.reference = reference;
            return this;
        }

        public TransactionStatus.TransactionStatusBuilder status(final Status status) {
            this.status = status;
            return this;
        }

        public TransactionStatus.TransactionStatusBuilder amount(final Double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionStatus.TransactionStatusBuilder fee(final Double fee) {
            this.fee = fee;
            return this;
        }

        public TransactionStatus build() {
            return new TransactionStatus(reference,status,amount,fee );
        }

    }
}
