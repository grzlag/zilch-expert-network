package com.zilch.zen.messagesender.chains.commands;

import com.zilch.zen.messagesender.chains.EmailRequestContext;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Component;

@Component
public class ContentGenerationCommand implements Command  {

    @Override
    public boolean execute(Context context) {
        EmailRequestContext emailContext = (EmailRequestContext) context;
        emailContext.setContent("""
                Congratulations! You have successfully pay for %s.
                Transferred cash: %s
            """.formatted(emailContext.getPurchaseName(), emailContext.getAmount())
        );
        return CONTINUE_PROCESSING;
    }
}
