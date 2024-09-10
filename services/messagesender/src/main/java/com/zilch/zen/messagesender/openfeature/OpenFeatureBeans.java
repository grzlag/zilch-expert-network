package com.zilch.zen.messagesender.openfeature;

import dev.openfeature.contrib.providers.flagd.FlagdOptions;
import dev.openfeature.contrib.providers.flagd.FlagdProvider;
import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.exceptions.OpenFeatureError;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeatureBeans {

// FIXME
    @Bean
    public OpenFeatureAPI openFeatureAPI() {
        final OpenFeatureAPI openFeatureAPI = OpenFeatureAPI.getInstance();
        try {
            openFeatureAPI.setProvider(new FlagdProvider(
                    FlagdOptions.builder()
                            .port(8013)
                            .host("localhost")
                            .build()
            ));
        } catch (OpenFeatureError e) {
            throw new RuntimeException("Failed to set OpenFeature provider", e);
        }
        return openFeatureAPI;
    }
}

