package de.rwth.idsg.bikeman.psinterface.dto.response;

import lombok.Data;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.07.2015
 */
public class CardKeyDTO {

    @Data
    public static class ReadOnly {
        private final String name, readKey;
    }

    @Data
    public static class Write {
        private final String name, readKey, writeKey, applicationKey;
    }

}
