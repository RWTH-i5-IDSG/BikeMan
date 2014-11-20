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

    public static ErrorType invalidSystem() {
        ErrorType e = new ErrorType();
        e.setCode(ErrorCodeType.SYS_REQUEST_NOT_PLAUSIBLE);
        e.setNonFatal(false);
        e.setSystemMessage("System ID is unknown");
        return e;
    }

    public static ErrorType notImplemented(String systemMsg, String userMsg) {
        ErrorType e = new ErrorType();
        e.setCode(ErrorCodeType.SYS_NOT_IMPLEMENTED);
        e.setNonFatal(true);
        if (systemMsg != null) e.setSystemMessage(systemMsg);
        if (userMsg != null) e.setUserMessage(userMsg);
        return e;
    }

    public static ErrorType invalidRequest(String systemMsg, String userMsg) {
        ErrorType e = new ErrorType();
        e.setCode(ErrorCodeType.SYS_REQUEST_NOT_PLAUSIBLE);
        e.setNonFatal(true);
        if (systemMsg != null) e.setSystemMessage(systemMsg);
        if (userMsg != null) e.setUserMessage(userMsg);
        return e;
    }

    public static ErrorType invalidUserToken(String systemMsg, String userMsg) {
        ErrorType e = new ErrorType();
        e.setCode(ErrorCodeType.AUTH_INVALID_TOKEN);
        e.setNonFatal(true);
        if (systemMsg != null) e.setSystemMessage(systemMsg);
        if (userMsg != null) e.setUserMessage(userMsg);
        return e;
    }

    public static ErrorType invalidProvider(String systemMsg, String userMsg) {
        ErrorType e = new ErrorType();
        e.setCode(ErrorCodeType.AUTH_PROVIDER_UNKNOWN);
        e.setNonFatal(true);
        if (systemMsg != null) e.setSystemMessage(systemMsg);
        if (userMsg != null) e.setUserMessage(userMsg);
        return e;
    }

    public static ErrorType backendFailed(String systemMsg, String userMsg) {
        ErrorType e = new ErrorType();
        e.setCode(ErrorCodeType.SYS_BACKEND_FAILED);
        e.setNonFatal(true);
        if (systemMsg != null) e.setSystemMessage(systemMsg);
        if (userMsg != null) e.setUserMessage(userMsg);
        return e;
    }
}