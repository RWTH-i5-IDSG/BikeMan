package de.rwth.idsg.bikeman.psinterface.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by swam on 01/08/14.
 */
public enum OperationState {
    OPERATIVE,      // When the item is functional and working and ready to serve
    INOPERATIVE;    // When the item is faulted and cannot be used

    @JsonValue // serialize
    public String value() {
        return name();
    }

    @JsonCreator // deserialize
    public static OperationState fromValue(String v) {
        for (OperationState c : OperationState.values()) {
            if (c.name().equalsIgnoreCase(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
