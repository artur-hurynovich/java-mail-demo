package com.hurynovich.java_mail_demo.enumeration;

public enum EmailProtocol {
    IMAPS("imaps");

    private final String protocolKey;
    private final String protocolValue;

    EmailProtocol(final String protocolValue) {
        this.protocolKey = "mail.store.protocol";
        this.protocolValue = protocolValue;
    }

    public String getProtocolKey() {
        return protocolKey;
    }

    public String getProtocolValue() {
        return protocolValue;
    }
}
