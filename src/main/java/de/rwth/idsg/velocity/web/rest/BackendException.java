package de.rwth.idsg.velocity.web.rest;

/**
 * Created by sgokay on 12.06.14.
 */
public class BackendException extends Exception {

    private static final long serialVersionUID = 1L;

    public BackendException(String message) {
        super(message);
    }

    public BackendException(String message, Throwable cause) {
        super(message, cause);
    }
}
