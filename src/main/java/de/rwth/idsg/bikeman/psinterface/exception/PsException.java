package de.rwth.idsg.bikeman.psinterface.exception;

import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

/**
 * Created by swam on 04/08/14.
 */
public class PsException extends DatabaseException {
    private static final long serialVersionUID = 1L;

    private PsErrorCode code;

    public PsException(String message, PsErrorCode code) {
        super(message);
        this.code = code;
    }

    public PsException(String message, Throwable cause, PsErrorCode code) {
        super(message, cause);
        this.code = code;
    }

    public PsErrorCode getErrorCode() {
        return code;
    }

}
