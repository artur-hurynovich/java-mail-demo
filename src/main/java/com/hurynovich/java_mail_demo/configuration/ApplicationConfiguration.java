package com.hurynovich.java_mail_demo.configuration;

import com.hurynovich.java_mail_demo.enumeration.EmailProtocol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public EmailProtocol emailProtocol() {
        return EmailProtocol.IMAPS;
    }
}
