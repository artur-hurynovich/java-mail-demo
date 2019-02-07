package com.hurynovich.java_mail_demo.controller;

import com.hurynovich.java_mail_demo.dto.EmailMessage;
import com.hurynovich.java_mail_demo.service.EmailMessageExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmailController {
    private final EmailMessageExtractor emailMessageExtractor;

    @Autowired
    public EmailController(final EmailMessageExtractor emailMessageExtractor) {
        this.emailMessageExtractor = emailMessageExtractor;
    }

    @GetMapping("/")
    public List<EmailMessage> getEmailMessagesBySenderEmailAddress(final @RequestParam("sender") String senderEmailAddress) {
        return emailMessageExtractor.getEmailMessagesBySenderEmailAddress(senderEmailAddress);
    }
}
