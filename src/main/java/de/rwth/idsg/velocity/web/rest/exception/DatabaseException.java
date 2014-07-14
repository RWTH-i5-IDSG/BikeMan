package de.rwth.idsg.velocity.web.rest.exception;

/**
 * Created by sgokay on 12.06.14.
 */
public class DatabaseException extends Exception {

    private static final long serialVersionUID = 1L;

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}