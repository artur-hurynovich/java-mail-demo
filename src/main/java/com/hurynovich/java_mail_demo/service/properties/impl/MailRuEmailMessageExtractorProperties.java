package com.hurynovich.java_mail_demo.service.properties.impl;

import com.hurynovich.java_mail_demo.service.properties.EmailMessageExtractorProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("mailRuProperties")
@Data
public class MailRuEmailMessageExtractorProperties implements EmailMessageExtractorProperties {
    @Value("${mail.ru.protocol.key}")
    private String protocolKey;
    @Value("${mail.ru.protocol.value}")
    private String protocolValue;
    @Value("${mail.ru.host}")
    private String host;
    @Value("${mail.ru.user}")
    private String user;
    @Value("${mail.ru.password}")
    private String password;
    @Value("${mail.ru.folder.inbox}")
    private String folderName;
}
