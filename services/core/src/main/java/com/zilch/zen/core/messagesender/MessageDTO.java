package com.zilch.zen.core.messagesender;

import com.zilch.zen.core.utils.PaymentMethod;
import com.zilch.zen.core.utils.WhichPayment;

import java.math.BigDecimal;

public record MessageDTO(
        String id,
        String email,
        String purchaseName,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        WhichPayment payment
) {
}
