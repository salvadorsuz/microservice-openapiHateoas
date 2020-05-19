package com.challenge.microservicechallenge.persistence.entitiy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="MONEY_MOVEMENT")
public class MoneyMovement implements Serializable {



    public MoneyMovement() {

    }

    public MoneyMovement(Long id, String reference, String iban, Date date, Double amount, Double fee, String description) {
        this.id = id;
        this.reference = reference;
        this.iban = iban;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="reference")
    private String reference;

    @Column(name="iban")
    private String iban;

    @Column(name="date")
    private Date date;

    @Column(name="amount")
    private Double amount;

    @Column(name="fee")
    private Double fee;

    @Column(name="description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
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

    public static MoneyMovement.MoneyMovementBuilder builder() {
        return new MoneyMovement.MoneyMovementBuilder();
    }

    public static class MoneyMovementBuilder {

        private Long id;

        private String reference;

        private String iban;

        private Date date;

        private Double amount;

        private Double fee;

        private String description;

        MoneyMovementBuilder() {
        }

        public MoneyMovement.MoneyMovementBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public MoneyMovement.MoneyMovementBuilder reference(final String reference) {
            this.reference = reference;
            return this;
        }

        public MoneyMovement.MoneyMovementBuilder iban(final String iban) {
            this.iban = iban;
            return this;
        }

        public MoneyMovement.MoneyMovementBuilder date(final Date date) {
            this.date = date;
            return this;
        }

        public MoneyMovement.MoneyMovementBuilder amount(final Double amount) {
            this.amount = amount;
            return this;
        }

        public MoneyMovement.MoneyMovementBuilder fee(final Double fee) {
            this.fee = fee;
            return this;
        }

        public MoneyMovement.MoneyMovementBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public MoneyMovement build() {
            return new MoneyMovement(id, reference, iban, date, amount, fee, description);
        }
    }

    @Override
    public String toString() {
        return "MoneyMovement{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", iban='" + iban + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", fee=" + fee +
                ", description='" + description + '\'' +
                '}';
    }
}
