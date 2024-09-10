package com.zilch.zen.messagesender.chains.commands;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MockedEmailSender {

    private final KafkaTemplate<String, String> kafkaTemplate; // why kafka? used temporary as simple and ready tool for e2e testing

    public MockedEmailSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmail(String recipient, String content) {
        kafkaTemplate.send("e2e-tests", content);

    }
}
