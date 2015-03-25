package de.rwth.idsg.bikeman.app.exception;


import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

public class AppException extends DatabaseException {
    private static final long serialVersionUID = 1L;

    private AppErrorCode code;

    public AppException(String message, AppErrorCode code) {
        super(message);
        this.code = code;
    }

    public AppException(String message, Throwable cause, AppErrorCode code) {
        super(message, cause);
        this.code = code;
    }

    public AppErrorCode getErrorCode() {
        return code;
    }

}
