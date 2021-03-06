package com.hurynovich.java_mail_demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class EmailMessage {
    private String senderEmailAddress;
    private String subject;
    private String messageText;
    private List<String> recipientEmailAddress;
    private LocalDateTime sentDate;
    private LocalDateTime receivedDate;
}
