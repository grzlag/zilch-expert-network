package com.zilch.zen.messagesender.messages;

import com.zilch.zen.messagesender.chains.EmailChainService;
import com.zilch.zen.messagesender.chains.EmailRequestContext;
import com.zilch.zen.messagesender.messages.model.MessageDTO;
import com.zilch.zen.messagesender.utils.JsonSerializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageTopicConsumer {

    private final EmailChainService emailChainService;
    private final JsonSerializer jsonSerializer;
    private final EmailContextBuilder emailContextBuilder;

    public MessageTopicConsumer(
            EmailChainService emailChainService,
            JsonSerializer jsonSerializer,
            EmailContextBuilder emailContextBuilder
    ) {
        this.emailChainService = emailChainService;
        this.jsonSerializer = jsonSerializer;
        this.emailContextBuilder = emailContextBuilder;
    }

    @KafkaListener(
            topics = "${spring.consumers.payments-send-message.topic-name}",
            groupId = "${spring.consumers.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(String message) {
        MessageDTO messageDTO = jsonSerializer.deserialize(message, MessageDTO.class);
        EmailRequestContext emailRequestContext = emailContextBuilder.buildEmailRequestContext(messageDTO);
        emailChainService.processEmail(emailRequestContext);
    }
}
