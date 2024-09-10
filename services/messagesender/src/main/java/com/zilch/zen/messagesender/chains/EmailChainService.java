package com.zilch.zen.messagesender.chains;

import com.zilch.zen.messagesender.chains.commands.AllowlistCheckCommand;
import com.zilch.zen.messagesender.chains.commands.ContentGenerationCommand;
import com.zilch.zen.messagesender.chains.commands.SendEmailCommand;
import org.apache.commons.chain.impl.ChainBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailChainService {

    private final ChainBase chain;

    @Autowired
    public EmailChainService(
            AllowlistCheckCommand allowlistCheckCommand,
            ContentGenerationCommand contentGenerationCommand,
            SendEmailCommand sendEmailCommand
    ) {
        chain = new ChainBase();
        chain.addCommand(allowlistCheckCommand);
        chain.addCommand(contentGenerationCommand);
        chain.addCommand(sendEmailCommand);
    }

    public void processEmail(EmailRequestContext context) {
        try {
            chain.execute(context);
        } catch (Exception e) {
            System.out.println("An error occurred while processing the email: " + e.getMessage());
        }
    }
}
