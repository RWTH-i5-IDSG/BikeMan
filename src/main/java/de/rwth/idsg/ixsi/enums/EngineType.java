package de.rwth.idsg.ixsi.enums;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 31.10.2014
 */
public enum EngineType {
    NONE("none"),               // Kein Antrieb (Muskelkraft)
    DIESEL("diesel"),           // Dieselmotor
    GASOLINE("gasoline"),       // Ottomotor
    ELECTRIC("electric"),       // Elektromotor
    LIQUID_GAS("liquidgas"),    // Flüssiggas
    HYDROGEN("hydrogen"),       // Wasserstoff
    HYBRID("hybrid");           // Hybridantrieb mit Elektro- und Verbrennungsmotor

    private final String value;

    private EngineType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
