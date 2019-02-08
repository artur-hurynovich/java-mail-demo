package com.hurynovich.java_mail_demo.service.email_message.impl;

import com.hurynovich.java_mail_demo.dto.EmailMessage;
import com.hurynovich.java_mail_demo.exception.EmailServiceException;
import com.hurynovich.java_mail_demo.service.email_message.EmailMessageService;
import com.hurynovich.java_mail_demo.util.EmailAddressToPlainText;
import com.hurynovich.java_mail_demo.util.TextFormatter;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailMessageServiceImpl implements EmailMessageService {
    @Override
    public List<EmailMessage> extractAllByFolder(final Folder folder) {
        try {
            folder.open(Folder.READ_ONLY);
            final List<Message> messagesBySenderEmailAddress =
                    getAllMessages(folder);
            return messagesBySenderEmailAddress.stream().map(this::convertMessageToEmailMessage).
                    collect(Collectors.toList());
        } catch (MessagingException e) {
            throw new EmailServiceException("Failed to extract email messages!" + e);
        } finally {
            closeFolder(folder);
        }
    }

    @Override
    public List<EmailMessage> extractAllByFolderAndSenderEmail(final Folder folder, final String senderEmailAddress) {
        try {
            folder.open(Folder.READ_ONLY);
            final List<Message> messagesBySenderEmailAddress =
                    getAllMessagesBySenderEmail(folder, senderEmailAddress);
            return messagesBySenderEmailAddress.stream().map(this::convertMessageToEmailMessage).
                    collect(Collectors.toList());
        } catch (MessagingException e) {
            throw new EmailServiceException("Failed to extract email messages!" + e);
        }
    }

    private List<Message> getAllMessages(final Folder folder) throws MessagingException {
        return Arrays.asList(folder.getMessages());
    }

    private List<Message> getAllMessagesBySenderEmail(final Folder folder, final String senderEmail)
            throws MessagingException{
        return Arrays.asList(folder.search(new SearchTerm() {
            @Override
            public boolean match(final Message message) {
                try {
                    return message.getFrom()[0].toString().contains(senderEmail);
                } catch (MessagingException e) {
                    throw new EmailServiceException(e);
                }
            }
        }));
    }

    private EmailMessage convertMessageToEmailMessage(final Message message) {
        final EmailMessage emailMessage = new EmailMessage();
        try {
            emailMessage.setSenderEmailAddress(EmailAddressToPlainText.convert(message.getFrom()[0].toString()));
            emailMessage.setSubject(TextFormatter.format(message.getSubject()));
            emailMessage.setMessageText(TextFormatter.format(getMessageText(message)));
            emailMessage.setRecipientEmailAddress(Arrays.stream(message.getAllRecipients()).
                    map(Address::toString).
                    map(EmailAddressToPlainText::convert).
                    collect(Collectors.toList()));
            emailMessage.setSentDate(
                    message.getSentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            emailMessage.setReceivedDate(
                    message.getReceivedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        } catch (MessagingException | IOException e) {
            throw new EmailServiceException("Failed to covert Message instance to EmailMessageInstance", e);
        }
        return emailMessage;
    }

    private String getMessageText(final Message message) throws MessagingException, IOException {
        String messageText;
        if (message.isMimeType("multipart/*")) {
            final MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            messageText = getTextFromMimeMultipart(mimeMultipart);
        } else {
            messageText = message.getContent().toString();
        }
        return messageText;
    }

    private String getTextFromMimeMultipart(final MimeMultipart mimeMultipart) throws MessagingException, IOException {
        final StringBuilder textBuilder = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            final BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.getContent() instanceof MimeMultipart){
                textBuilder.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            } else {
                textBuilder.append(bodyPart.getContent().toString());
                break;
            }
        }
        return textBuilder.toString();
    }

    private void closeFolder(final Folder folder) {
        try {
            folder.close();
        } catch (final MessagingException e) {
            throw new EmailServiceException("Failed to close folder " + folder.getName() + "! " + e);
        }
    }
}
