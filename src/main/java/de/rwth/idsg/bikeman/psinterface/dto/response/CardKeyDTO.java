package de.rwth.idsg.bikeman.psinterface.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.07.2015
 */
public class CardKeyDTO {

    @Data
    @Builder
    public static final class ReadOnly {
        private final String name, readKey;
    }

    @Data
    @Builder
    public static final class Write {
        private final String name, readKey, writeKey, applicationKey;
    }

}
