package de.rwth.idsg.bikeman.domain;

/**
 * Created by max on 26/06/15.
 */
public enum TransactionStatus {
    PENDING("pending"),
    PROCESSED("processed");

    private final String value;

    TransactionStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TransactionStatus fromValue(String str) {
        for (TransactionStatus c : TransactionStatus.values()) {
            if (c.value.equalsIgnoreCase(str)) {
                return c;
            }
        }
        throw new IllegalArgumentException(str);
    }
}
