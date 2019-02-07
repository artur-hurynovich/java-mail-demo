package com.hurynovich.java_mail_demo.service.properties.impl;

import com.hurynovich.java_mail_demo.service.properties.EmailMessageExtractorProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("gmailComProperties")
@Data
public class GmailComEmailMessageExtractorProperties implements EmailMessageExtractorProperties {
    @Value("${gmail.com.protocol.key}")
    private String protocolKey;
    @Value("${gmail.com.protocol.value}")
    private String protocolValue;
    @Value("${gmail.com.host}")
    private String host;
    @Value("${gmail.com.user}")
    private String user;
    @Value("${gmail.com.password}")
    private String password;
    @Value("${gmail.com.folder.inbox}")
    private String folderName;
}
