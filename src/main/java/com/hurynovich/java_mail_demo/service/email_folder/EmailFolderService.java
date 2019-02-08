package com.hurynovich.java_mail_demo.service.email_folder;

import javax.mail.Folder;
import java.util.List;

public interface EmailFolderService {
    List<Folder> findAllFolders();

    Folder findFolderByName(final String folderName);
}
