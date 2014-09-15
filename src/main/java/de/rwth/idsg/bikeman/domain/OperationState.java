package de.rwth.idsg.bikeman.domain;

/**
 * Created by sgokay on 20.05.14.
 */
public enum OperationState {
    OPERATIVE,  // When the item is functional and working and ready to serve
    INOPERATIVE // When the item is faulted and cannot be used
}