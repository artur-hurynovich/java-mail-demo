package com.hurynovich.java_mail_demo.util;

public class EmailAddressToPlainText {
    private EmailAddressToPlainText() {
        throw new IllegalStateException("Utility class!");
    }

    public static String convert(final String emailAddress) {
        final int start = emailAddress.indexOf('<') + 1;
        final int end = emailAddress.indexOf('>');
        if (start > 0 && start < end && end < emailAddress.length()) {
            return emailAddress.substring(start, end);
        } else {
            return emailAddress;
        }
    }
}
