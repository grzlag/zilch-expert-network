package com.zilch.zen.messagesender.messages;

import com.zilch.zen.messagesender.chains.EmailRequestContext;
import com.zilch.zen.messagesender.messages.model.MessageDTO;
import org.springframework.stereotype.Component;

@Component
public class EmailContextBuilder {

    public EmailRequestContext buildEmailRequestContext(MessageDTO messageDTO) {
        EmailRequestContext emailRequestContext = new EmailRequestContext();
        emailRequestContext.setRecipient(messageDTO.email());
        emailRequestContext.setSent(false);
        emailRequestContext.setContent(messageDTO.email());
        emailRequestContext.setAmount(messageDTO.amount());
        emailRequestContext.setPurchaseName(messageDTO.purchaseName());
        emailRequestContext.setPaymentMethod(messageDTO.paymentMethod());
        return emailRequestContext;
    }
}
