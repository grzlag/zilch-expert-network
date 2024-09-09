package com.zilch.zen.core.messagesender;

import com.zilch.zen.core.payments.kafka.PaymentDTO;
import com.zilch.zen.core.users.entity.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MessageDTOMapper {

    public MessageDTO mapToMessageDTO(PaymentDTO paymentDTO, User user) {
        return new MessageDTO(
                UUID.randomUUID().toString(),
                user.getEmail(),
                paymentDTO.purchaseName(),
                paymentDTO.amount(),
                paymentDTO.paymentMethod(),
                paymentDTO.payment()
        );
    }
}
