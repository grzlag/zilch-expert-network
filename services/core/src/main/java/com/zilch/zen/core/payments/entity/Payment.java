package com.zilch.zen.core.payments.entity;

import com.zilch.zen.core.purchases.entity.Purchase;
import com.zilch.zen.core.utils.WhichPayment;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    private String id;

    private String purchaseName;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private WhichPayment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    public Payment() {}

    public Payment(String id, String purchaseName, BigDecimal amount, WhichPayment payment, Purchase purchase) {
        this.id = id;
        this.purchaseName = purchaseName;
        this.amount = amount;
        this.payment = payment;
        this.purchase = purchase;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public WhichPayment getPayment() {
        return payment;
    }

    public void setPayment(WhichPayment payment) {
        this.payment = payment;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
}