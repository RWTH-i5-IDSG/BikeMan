package de.rwth.idsg.bikeman.web.rest.exception;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 12.06.2014
 */
public class DatabaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}