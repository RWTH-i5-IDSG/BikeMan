package de.rwth.idsg.bikeman;

/**
 * Holds the global configuration parameters for the whole application.
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 29.09.2014
 */
public final class ApplicationConfig {
    private ApplicationConfig() {}

    /**
     * Configuration for IXSI / WebSocket
     */
    public final class IXSI {
        public static final String WS_ENDPOINT  = "/ws";
        public static final String JAXB_CONTEXT_PATH = "de.rwth.idsg.bikeman.ixsi.schema";
    }
}