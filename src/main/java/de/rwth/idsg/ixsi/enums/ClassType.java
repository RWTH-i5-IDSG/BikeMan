package de.rwth.idsg.ixsi.enums;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 31.10.2014
 */
public enum ClassType {
    BIKE("bike"),                   // Fahrrad
    MOTORCYCLE("motorcycle"),       // Motorrad, Motorroller
    MICRO("micro"),                 // Kleinstwagen (z.B. SMART)
    MINI("mini"),                   // Kleinwagen (z.B. Opel Corsa)
    SMALL("small"),                 // Kompaktwagen (z.B. VW Golf)
    MEDIUM("medium"),               // Mittelklassewagen (z.B. Audi A4)
    LARGE("large"),                 // Oberklassewagen (z.B. Audi A8)
    VAN("van"),                     // Van (z.B. VW Bus)
    TRANSPORTER("transporter");     // Transporter (z.B. Mercedes Benz Sprinter)

    private final String value;

    private ClassType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
