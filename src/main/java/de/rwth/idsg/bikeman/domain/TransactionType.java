package de.rwth.idsg.bikeman.domain;

/**
 * Created by max on 26/06/15.
 */
public enum TransactionType {
    START("start"),
    STOP("stop");

    private final String value;

    TransactionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TransactionType fromValue(String str) {
        for (TransactionType c : TransactionType.values()) {
            if (c.value.equalsIgnoreCase(str)) {
                return c;
            }
        }
        throw new IllegalArgumentException(str);
    }
}
