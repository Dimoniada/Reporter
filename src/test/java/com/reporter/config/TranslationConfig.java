package com.reporter.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class TranslationConfig {
    @Bean
    public MessageSource messageSource() {
        final var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("translation/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
