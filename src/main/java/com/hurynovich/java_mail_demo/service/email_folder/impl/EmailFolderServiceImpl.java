package com.hurynovich.java_mail_demo.service.email_folder.impl;

import com.hurynovich.java_mail_demo.enumeration.EmailProtocol;
import com.hurynovich.java_mail_demo.exception.EmailServiceException;
import com.hurynovich.java_mail_demo.service.email_folder.EmailFolderService;
import com.hurynovich.java_mail_demo.service.properties.EmailServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class EmailFolderServiceImpl implements EmailFolderService {
    private final Store store;

    @Autowired
    public EmailFolderServiceImpl(final EmailProtocol emailProtocol,
                                  final @Qualifier("mailRuProperties") EmailServiceProperties emailServiceProperties)
            throws MessagingException {
        final Properties protocolProperties = new Properties();
        protocolProperties.setProperty(emailProtocol.getProtocolKey(), emailProtocol.getProtocolValue());
        final Session session = Session.getInstance(protocolProperties);
        store = session.getStore();
        store.connect(emailServiceProperties.getHost(), emailServiceProperties.getUser(), emailServiceProperties.getPassword());
    }

    @Override
    public List<Folder> findAllFolders() {
        try {
            return Arrays.asList(store.getDefaultFolder().list());
        } catch (final MessagingException e) {
            throw new EmailServiceException("Failed to find email service folders! " + e);
        }
    }

    @Override
    public Folder findFolderByName(final String folderName) {
        try {
            return store.getFolder(folderName);
        } catch (final MessagingException e) {
            throw new EmailServiceException("Failed to find email service folder " + folderName + "! " + e);
        }
    }
}
