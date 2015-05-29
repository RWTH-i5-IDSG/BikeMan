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
        return setFields(ErrorCodeType.SYS_NOT_IMPLEMENTED, systemMsg, userMsg);
    }

    public static ErrorType notAllowedAnonym(String systemMsg, String userMsg) {
        return setFields(ErrorCodeType.AUTH_ANON_NOT_ALLOWED, systemMsg, userMsg);
    }

    public static ErrorType invalidAuth(String systemMsg, String userMsg) {
        return setFields(ErrorCodeType.AUTH_NOT_AUTHORIZED, systemMsg, userMsg);
    }

    public static ErrorType invalidRequest(String systemMsg, String userMsg) {
        return setFields(ErrorCodeType.SYS_REQUEST_NOT_PLAUSIBLE, systemMsg, userMsg);
    }

    public static ErrorType invalidProvider(String systemMsg, String userMsg) {
        return setFields(ErrorCodeType.AUTH_PROVIDER_UNKNOWN, systemMsg, userMsg);
    }

    public static ErrorType backendFailed(String systemMsg, String userMsg) {
        return setFields(ErrorCodeType.SYS_BACKEND_FAILED, systemMsg, userMsg);
    }

    public static ErrorType bookingTargetNotAvail(String systemMsg, String userMsg) {
        return setFields(ErrorCodeType.BOOKING_TARGET_NOT_AVAILABLE, systemMsg, userMsg);
    }

    private static ErrorType setFields(ErrorCodeType c, String systemMsg, String userMsg) {
        ErrorType e = new ErrorType();
        e.setCode(c);
        e.setNonFatal(true);
        e.setSystemMessage(systemMsg);
        e.setUserMessage(userMsg);
        return e;
    }
}