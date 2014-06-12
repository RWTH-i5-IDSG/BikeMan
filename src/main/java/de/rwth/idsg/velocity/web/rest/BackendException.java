package de.rwth.idsg.velocity.web.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by sgokay on 12.06.14.
 */
@AllArgsConstructor
public class BackendException extends Exception {

    private static final long serialVersionUID = 1L;

    @Getter @Setter
    private String message;

}
