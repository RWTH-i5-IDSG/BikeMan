package de.rwth.idsg.bikeman.psinterface.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by swam on 01/08/14.
 */
public enum LogsUpdateState {
    UPLOADED,
    UPLOAD_FAILED;

    @JsonValue // serialize
    public String value() {
        return name();
    }

    @JsonCreator // deserialize
    public static LogsUpdateState fromValue(String v) {
        for (LogsUpdateState c : LogsUpdateState.values()) {
            if (c.name().equalsIgnoreCase(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
