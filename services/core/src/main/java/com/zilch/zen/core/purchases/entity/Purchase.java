package com.zilch.zen.core.purchases.entity;

import com.zilch.zen.core.payments.entity.Payment;
import com.zilch.zen.core.users.entity.User;
import com.zilch.zen.core.utils.PaymentMethod;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    private String id;

    private String purchaseName;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    // Many purchases can belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // One purchase can have many payments
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    public Purchase() {
    }

    public Purchase(String id, String purchaseName, PaymentMethod paymentMethod, User user) {
        this.id = id;
        this.purchaseName = purchaseName;
        this.paymentMethod = paymentMethod;
        this.user = user;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}

