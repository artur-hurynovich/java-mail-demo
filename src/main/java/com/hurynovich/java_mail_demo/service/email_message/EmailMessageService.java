package com.hurynovich.java_mail_demo.service.email_message;

import com.hurynovich.java_mail_demo.dto.EmailMessage;

import javax.mail.Folder;
import java.util.List;

public interface EmailMessageService {
    List<EmailMessage> extractAllByFolder(final Folder folder);
    List<EmailMessage> extractAllByFolderAndSenderEmail(final Folder folder, final String senderEmail);
}
