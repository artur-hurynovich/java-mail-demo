package com.hurynovich.java_mail_demo.service.properties.impl;

import com.hurynovich.java_mail_demo.service.properties.EmailServiceProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("gmailComProperties")
@Data
public class GmailComEmailServiceProperties implements EmailServiceProperties {
    @Value("${gmail.com.host}")
    private String host;
    @Value("${gmail.com.user}")
    private String user;
    @Value("${gmail.com.password}")
    private String password;
}
