package com.hurynovich.java_mail_demo.exception;

public class EmailServiceException extends RuntimeException {
    public EmailServiceException() {
        super();
    }

    public EmailServiceException(final String message) {
        super(message);
    }

    public EmailServiceException(final Exception e) {
        super(e);
    }

    public EmailServiceException(final String message, final Exception e) {
        super(message, e);
    }
}
