package de.rwth.idsg.bikeman.psinterface.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by swam on 01/08/14.
 */
public enum FirmwareUpdateState {
    DOWNLOAD_FAILED,
    INSTALLATION_FAILED,
    INSTALLED;

    @JsonValue // serialize
    public String value() {
        return name();
    }

    @JsonCreator // deserialize
    public static FirmwareUpdateState fromValue(String v) {
        for (FirmwareUpdateState c : FirmwareUpdateState.values()) {
            if (c.name().equalsIgnoreCase(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
