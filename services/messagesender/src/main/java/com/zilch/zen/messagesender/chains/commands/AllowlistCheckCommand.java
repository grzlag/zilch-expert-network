package com.zilch.zen.messagesender.chains.commands;

import com.zilch.zen.messagesender.chains.EmailRequestContext;
import dev.openfeature.sdk.*;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AllowlistCheckCommand implements Command {

    @Value("${global-management.allowed-emails-property-name}")
    private String allowedEmailsPropertyName;

    private final OpenFeatureAPI openFeatureAPI;

    @Autowired
    public AllowlistCheckCommand(OpenFeatureAPI openFeatureAPI) {
        this.openFeatureAPI = openFeatureAPI;
    }

    @Override
    public boolean execute(Context context) {
        EmailRequestContext emailContext = (EmailRequestContext) context;
        if (isAccessEnabled(emailContext.getRecipient())) {
            System.out.println("Email is allowlisted.");
            return CONTINUE_PROCESSING;
        } else {
            System.out.println("Email  is not allowlisted. Stopping the process.");
            return PROCESSING_COMPLETE;
        }
    }

    public boolean isAccessEnabled(String email) {
        Client client = openFeatureAPI.getClient();

        MutableContext context = new MutableContext();
        context.add("email", email);

        FlagEvaluationDetails<Boolean> flagDetails = client.getBooleanDetails(allowedEmailsPropertyName, false, context);

        return flagDetails.getValue();
    }

}