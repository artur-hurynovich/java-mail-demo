package com.hurynovich.java_mail_demo.service;

import com.hurynovich.java_mail_demo.dto.EmailMessage;

import java.util.List;

public interface EmailMessageExtractor {
    List<EmailMessage> getEmailMessagesBySenderEmailAddress(final String senderEmailAddress);
}
