# java-mail-demo
Application demonstrates retrieving e-mails from different e-mail services.

To use the application, first you should fill corresponding values in application.properties file (according to chosen e-mail service):
- *.user;
- *.password.

All tou need to switch between e-mail services - is just to change @Qualifier annotation value in GenericEmailMessageExtractor class.

If you want to add new e-mail service, first, you need to add properties to application.properties file, and then add new implementation of EmailMessageExtractorProperties interface.

After starting application go to: localhost:8080//?sender=[sender_email], to retrieve all e-mails from sender_email in JSON representation.

NOTE: If you use gmail.com, to access e-mails from application, you should turn on "Less secure app access" in "Security" Settings of your Google Account.
