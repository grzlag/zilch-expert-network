package com.zilch.zen.core.payments.kafka;

import com.zilch.zen.core.utils.PaymentMethod;
import com.zilch.zen.core.utils.WhichPayment;

import java.math.BigDecimal;

public record PaymentDTO(
        String id,
        String userId,
        String purchaseId,
        String purchaseName,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        WhichPayment payment
) {
}

