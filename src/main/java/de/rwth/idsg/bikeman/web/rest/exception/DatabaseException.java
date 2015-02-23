package de.rwth.idsg.bikeman.web.rest.exception;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 12.06.2014
 */
public class DatabaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String errorCode = "";

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}