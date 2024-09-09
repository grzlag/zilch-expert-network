package com.zilch.zen.core.payments.kafka;

import com.zilch.zen.core.messagesender.EmailMessageSender;
import com.zilch.zen.core.payments.PaymentService;
import com.zilch.zen.core.utils.JsonSerializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentTopicConsumer {

    private final JsonSerializer jsonSerializer;
    private final PaymentService paymentService;

    public PaymentTopicConsumer(JsonSerializer jsonSerializer, PaymentService paymentService) {
        this.jsonSerializer = jsonSerializer;
        this.paymentService = paymentService;
    }

    @KafkaListener(
            topics = "${spring.kafka.consumers.payments.topic-name}",
            groupId = "${spring.kafka.consumers.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(String message) {
        PaymentDTO paymentDTO = jsonSerializer.deserialize(message, PaymentDTO.class);
        paymentService.processPayment(paymentDTO);
    }

}
