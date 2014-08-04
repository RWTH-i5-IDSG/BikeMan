package de.rwth.idsg.velocity.psinterface.exception;

/**
 * Created by swam on 04/08/14.
 */
public class PSInterfaceException extends Exception {
    private static final long serialVersionUID = 1L;

    private String errorCode;

    public PSInterfaceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PSInterfaceException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
