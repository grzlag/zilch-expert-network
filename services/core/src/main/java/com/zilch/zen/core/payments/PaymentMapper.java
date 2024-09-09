package com.zilch.zen.core.payments;

import com.zilch.zen.core.payments.entity.Payment;
import com.zilch.zen.core.payments.kafka.PaymentDTO;
import com.zilch.zen.core.purchases.entity.Purchase;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentDTO paymentDTO, Purchase purchase) {
        Payment payment = new Payment();
        payment.setId(paymentDTO.id());
        payment.setAmount(paymentDTO.amount());
        payment.setPayment(paymentDTO.payment());
        payment.setPurchase(purchase);
        payment.setPurchaseName(purchase.getPurchaseName());
        return payment;
    }

}
