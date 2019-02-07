package com.hurynovich.java_mail_demo.service.impl;

import com.hurynovich.java_mail_demo.dto.EmailMessage;
import com.hurynovich.java_mail_demo.exception.EmailExtractingException;
import com.hurynovich.java_mail_demo.service.EmailMessageExtractor;
import com.hurynovich.java_mail_demo.service.properties.EmailMessageExtractorProperties;
import com.hurynovich.java_mail_demo.util.EmailAddressToPlainText;
import com.hurynovich.java_mail_demo.util.TextFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class GenericEmailMessageExtractor implements EmailMessageExtractor {
    private final EmailMessageExtractorProperties extractorProperties;
    private Store store;

    @Autowired
    public GenericEmailMessageExtractor(final @Qualifier("gmailComProperties") EmailMessageExtractorProperties extractorProperties)
            throws MessagingException {
        this.extractorProperties = extractorProperties;
        final Properties protocolProperties = new Properties();
        protocolProperties.setProperty(extractorProperties.getProtocolKey(), extractorProperties.getProtocolValue());
        final Session session = Session.getInstance(protocolProperties);
        store = session.getStore();
        store.connect(extractorProperties.getHost(), extractorProperties.getUser(), extractorProperties.getPassword());
    }

    @Override
    public List<EmailMessage> getEmailMessagesBySenderEmailAddress(final String senderEmailAddress) {
        try (final Folder inboxFolder = store.getFolder(extractorProperties.getFolderName())) {
            inboxFolder.open(Folder.READ_ONLY);
            final List<Message> messagesBySenderEmailAddress =
                    getMessagesBySenderEmailAddress(inboxFolder, senderEmailAddress);
            return messagesBySenderEmailAddress.stream().map(this::convertMessageToEmailMessage).
                    collect(Collectors.toList());
        } catch (MessagingException e) {
            throw new EmailExtractingException("Failed to extract email!" + e);
        }
    }

    private List<Message> getMessagesBySenderEmailAddress(final Folder inboxFolder, final String senderEmailAddress)
            throws MessagingException{
        return Arrays.asList(inboxFolder.search(new SearchTerm() {
            @Override
            public boolean match(final Message message) {
                try {
                    return message.getFrom()[0].toString().contains(senderEmailAddress);
                } catch (MessagingException e) {
                    throw new EmailExtractingException(e);
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
            throw new EmailExtractingException("Failed to covert Message instance to EmailMessageInstance", e);
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
}
