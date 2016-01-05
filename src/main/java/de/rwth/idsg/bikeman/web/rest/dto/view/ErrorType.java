package de.rwth.idsg.bikeman.web.rest.dto.view;

/**
 * Created by Wolfgang Kluth on 15/12/15.
 */
public enum ErrorType {
    STATION_ERROR("stationError"),
    SLOT_ERROR("slotError"),
    PEDELEC_ERROR("pedelecError");

    private final String value;

    ErrorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ErrorType fromValue(String str) {
        for (ErrorType c: ErrorType.values()) {
            if (c.value.equalsIgnoreCase(str)) {
                return c;
            }
        }
        throw new IllegalArgumentException(str);
    }
}
