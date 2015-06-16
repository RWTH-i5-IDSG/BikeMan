package de.rwth.idsg.bikeman.psinterface.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by swam on 27/04/15.
 */
public enum AccountState {
    HAS_PEDELEC,
    HAS_NO_PEDELEC;

    @JsonValue // serialize
    public String value() {
        return name();
    }

    @JsonCreator // deserialize
    public static AccountState fromValue(String v) {
        for (AccountState c : AccountState.values()) {
            if (c.name().equalsIgnoreCase(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
