package com.zilch.zen.messagesender.messages.model;


import com.zilch.zen.messagesender.utils.PaymentMethod;
import com.zilch.zen.messagesender.utils.WhichPayment;

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
