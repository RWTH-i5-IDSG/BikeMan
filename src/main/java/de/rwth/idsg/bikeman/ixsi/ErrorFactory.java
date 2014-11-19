package de.rwth.idsg.bikeman.ixsi;

import de.rwth.idsg.bikeman.ixsi.schema.ErrorCodeType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;

/**
 * Error generation should be handled by a central component for better control over the codes and messages.
 * This class just does that.
 *
 * TODO : define all the codes and messages
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 23.10.2014
 */
public final class ErrorFactory {
    private ErrorFactory() {}

    public static ErrorType requestNotSupported() {
        ErrorType e = new ErrorType();
        e.setNonFatal(false);
        e.setCode(ErrorCodeType.SYS_NOT_IMPLEMENTED);
        e.setSystemMessage("");
        e.setUserMessage("");
        return e;
    }

    public static ErrorType invalidRequest() {
        ErrorType e = new ErrorType();
        e.setNonFatal(false);
        e.setCode(ErrorCodeType.SYS_REQUEST_NOT_PLAUSIBLE);
        e.setSystemMessage("");
        e.setUserMessage("");
        return e;
    }

    public static ErrorType invalidSystem() {
        ErrorType e = new ErrorType();
        e.setNonFatal(false);
        e.setCode(ErrorCodeType.SYS_REQUEST_NOT_PLAUSIBLE);
        e.setSystemMessage("");
        e.setUserMessage("System ID is unknown");
        return e;
    }

    public static ErrorType invalidUserAuth() {
        ErrorType e = new ErrorType();
        e.setNonFatal(false);
        e.setCode(ErrorCodeType.AUTH_NOT_AUTHORIZED);
        e.setSystemMessage("");
        e.setUserMessage("");
        return e;
    }

    public static ErrorType backendFailed() {
        ErrorType e = new ErrorType();
        e.setNonFatal(false);
        e.setCode(ErrorCodeType.SYS_BACKEND_FAILED);
        e.setSystemMessage("Exception occurred");
        e.setUserMessage("");
        return e;
    }
}