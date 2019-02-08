package com.hurynovich.java_mail_demo.controller;

import com.hurynovich.java_mail_demo.dto.EmailMessage;
import com.hurynovich.java_mail_demo.service.email_folder.EmailFolderService;
import com.hurynovich.java_mail_demo.service.email_message.EmailMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Folder;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmailController {
    private final EmailFolderService emailFolderService;
    private final EmailMessageService emailMessageService;

    @Autowired
    public EmailController(final EmailFolderService emailFolderService, final EmailMessageService emailMessageService) {
        this.emailFolderService = emailFolderService;
        this.emailMessageService = emailMessageService;
    }

    @GetMapping("/folder/all")
    public List<String> getFolderNames() {
        return emailFolderService.findAllFolders().stream().map(Folder::getName).collect(Collectors.toList());
    }

    @GetMapping("/messages")
    public List<EmailMessage> getEmailMessages(final @RequestParam(value = "folder") String folderName,
                                               final @RequestParam(value = "sender", required = false) String senderEmail) {
        final Folder folder = emailFolderService.findFolderByName(folderName);
        if (senderEmail == null) {
            return emailMessageService.extractAllByFolder(folder);
        } else {
            return emailMessageService.extractAllByFolderAndSenderEmail(folder, senderEmail);
        }
    }
}
