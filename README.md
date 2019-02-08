# java-mail-demo
Application demonstrates retrieving e-mails from different e-mail services.

To use the application, first you should fill corresponding values in application.properties file (according to chosen e-mail service):
- *.user;
- *.password.

All you need to switch between e-mail services - is just to change @Qualifier annotation value in GenericEmailMessageExtractor class.

If you want to add new e-mail service, first, you need to add properties to application.properties file, and then add new implementation of EmailServiceProperties interface.

Use the following requests:
- localhost:8080/folder/all - to retrieve all e-mail service folders names;
- localhost:8080/messages?folder=[folder_name] - to retrieve all messages from the folder folder_name;
- localhost:8080/messages?folder=[folder_name]&sender=[sender_email] - to retrieve all messages from the folder folder_name, received from sender_email.

NOTE: If you use gmail.com, to access e-mails from application, you should turn on "Less secure app access" in "Security" Settings of your Google Account.
