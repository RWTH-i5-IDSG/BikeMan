package de.rwth.idsg.ixsi.enums;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 31.10.2014
 */
public enum AttributeClassType {
    TRAILER_HITCH("trailer_hitch"),     // Anhängerkupplung
    AUTOMATIC("automatic"),             // Automatikgetriebe
    CONVERTIBLE("convertible"),         // Cabriolet
    AIR_CONDITION("air_condition"),     // Klimaanlage
    NAVIGATION("navigation"),           // Navigationssystem
    CRUISE_CONTROL("cruise_control"),   // Tempomat
    WINTER_TYRES("winter_tyres"),       // Winter- bzw. Ganzjahresreifen

    CHILD_SEAT_0("child_seat_0"),   // Babyschale
    CHILD_SEAT_1("child_seat_1"),   // Kindersitz (9-18kg)
    CHILD_SEAT_4("child_seat_4"),   // Kindersitz (15-36kg)

    UTILITY("utility"), // Kombi
    DOORS_4("doors_4"), // 4/5-Türer

    SEATS_9("seats_9"), // Mindestens 9 Sitze
    SEATS_7("seats_7"), // Mindestens 7 Sitze
    SEATS_5("seats_5"), // Mindestens 5 Sitze
    SEATS_4("seats_4"); // Mindestens 4 Sitze

    private final String value;

    private AttributeClassType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
