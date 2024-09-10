package com.zilch.zen.messagesender.chains.commands;

import com.zilch.zen.messagesender.chains.EmailRequestContext;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendEmailCommand implements Command {

    private final MockedEmailSender mockedEmailSender;

    public SendEmailCommand(MockedEmailSender mockedEmailSender) {
        this.mockedEmailSender = mockedEmailSender;
    }

    @Override
    public boolean execute(Context context) {
        EmailRequestContext emailContext = (EmailRequestContext) context;
        System.out.println("Sending email to " + emailContext.getRecipient() + " with content: " + emailContext.getContent());
        mockedEmailSender.sendEmail(emailContext.getRecipient(), emailContext.getContent());
        emailContext.setSent(true);
        return CONTINUE_PROCESSING;
    }
}