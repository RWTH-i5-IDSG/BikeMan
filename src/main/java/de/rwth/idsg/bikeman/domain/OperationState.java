package de.rwth.idsg.bikeman.domain;

/**
 * Created by sgokay on 20.05.14.
 */
public enum OperationState {
    OPERATIVE("operative"),     // When the item is functional and working and ready to serve
    INOPERATIVE("inoperative"), // When the item is faulted and cannot be used
    DELETED("deleted");         // Mark the item as deleted instead of actually deleting it (there might be dependencies)

    private final String value;

    OperationState(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OperationState fromValue(String str) {
        for (OperationState c : OperationState.values()) {
            if (c.value.equalsIgnoreCase(str)) {
                return c;
            }
        }
        throw new IllegalArgumentException(str);
    }
}
