package com.challenge.microservicechallenge.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "All details about the transaction status")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionStatusDto {

    @Schema(description = "The transaction unique reference number in our system")
    private String reference;

    @Schema(description = "Status of the transaction. Can be: PENDING, SETTLED, INVALID, FUTURE")
    private StatusDto status;

    @Schema(description = "Amount of the transaction. Mandatory. Can be positive or negative")
    private Double amount;

    @Schema(description = "Fee that will be deducted from the amount. If present then must be positive or Zero")
    private Double fee;

    public TransactionStatusDto() {
    }

    public TransactionStatusDto(String reference, StatusDto status, Double amount, Double fee) {
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

    public StatusDto getStatus() {
        return status;
    }

    public void setStatus(StatusDto status) {
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

    public static TransactionStatusDto.TransactionStatusBuilder builder() {
        return new TransactionStatusDto.TransactionStatusBuilder();
    }

    public static class TransactionStatusBuilder {

        private String reference;

        private StatusDto status;

        private Double amount;

        private Double fee;

        TransactionStatusBuilder() {
        }

        public TransactionStatusDto.TransactionStatusBuilder reference(final String reference) {
            this.reference = reference;
            return this;
        }

        public TransactionStatusDto.TransactionStatusBuilder status(final StatusDto status) {
            this.status = status;
            return this;
        }

        public TransactionStatusDto.TransactionStatusBuilder amount(final Double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionStatusDto.TransactionStatusBuilder fee(final Double fee) {
            this.fee = fee;
            return this;
        }

        public TransactionStatusDto build() {
            return new TransactionStatusDto(reference,status,amount,fee );
        }

    }
}
