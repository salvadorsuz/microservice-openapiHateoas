package com.challenge.microservicechallenge.service.model;




public class TransactionStatus {

    private String reference;

    private Status status;

    private Double amount;

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

    public static TransactionStatusBuilder builder() {
        return new TransactionStatusBuilder();
    }

    public static class TransactionStatusBuilder {

        private String reference;

        private Status status;

        private Double amount;

        private Double fee;

        TransactionStatusBuilder() {
        }

        public TransactionStatusBuilder reference(final String reference) {
            this.reference = reference;
            return this;
        }

        public TransactionStatusBuilder status(final Status status) {
            this.status = status;
            return this;
        }

        public TransactionStatusBuilder amount(final Double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionStatusBuilder fee(final Double fee) {
            this.fee = fee;
            return this;
        }

        public TransactionStatus build() {
            return new TransactionStatus(reference,status,amount,fee );
        }

    }
}
