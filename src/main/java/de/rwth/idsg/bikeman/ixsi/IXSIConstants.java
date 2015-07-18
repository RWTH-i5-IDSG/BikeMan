package de.rwth.idsg.bikeman.ixsi;

import xjc.schema.ixsi.ClassType;
import xjc.schema.ixsi.ConsumptionClassType;
import xjc.schema.ixsi.EngineType;
import xjc.schema.ixsi.Language;

/**
 * Created by max on 07/10/14.
 */
public final class IXSIConstants {
    private IXSIConstants() {}

    public static final int MAX_TEXT_MSG_SIZE = 8388608;

    public static final String XML_SCHEMA_FILE = "IXSI-with-enums.xsd";

    public static final ClassType bookeeClassType = ClassType.BIKE;
    public static final EngineType engineType = EngineType.ELECTRIC;
    public static final ConsumptionClassType consumptionClass = ConsumptionClassType.DURATION;
    public static final Language DEFAULT_LANGUAGE = new Language().withValue("DE");

    public static final String BOOKING_ID_DELIMITER = "%#";

    public final class Provider {
        public static final String id = "Velocity";
        public static final String name = "Velocity";
        public static final String shortName = "Velocity";
        public static final String url = "http://www.velocity-aachen.de/";
        public static final String logoUrl = "";
        public static final String interAppBaseUrl = "";
        public static final String webAppBaseUrl = "";
    }

    public final class PlaceGroup {
        public static final String id = "Velocity-Aachen";
    }
}
