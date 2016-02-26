package de.rwth.idsg.bikeman.ixsi;

import xjc.schema.ixsi.ErrorCodeType;
import xjc.schema.ixsi.ErrorType;

/**
 * Error generation should be handled by a central component for better control over the codes and messages.
 * This class just does that.
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 23.10.2014
 */
public final class ErrorFactory {
    private ErrorFactory() {}

    public static class Auth {
        public static ErrorType unknownProvider(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.AUTH_PROVIDER_UNKNOWN, systemMsg, userMsg);
        }
        public static ErrorType invalidPass(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.AUTH_INVALID_PASSWORD, systemMsg, userMsg);
        }
        public static ErrorType invalidToken(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.AUTH_INVALID_TOKEN, systemMsg, userMsg);
        }
        public static ErrorType invalidSession(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.AUTH_SESSION_INVALID, systemMsg, userMsg);
        }
        public static ErrorType notAnonym(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.AUTH_ANON_NOT_ALLOWED, systemMsg, userMsg);
        }
        public static ErrorType notAuthorized(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.AUTH_NOT_AUTHORIZED, systemMsg, userMsg);
        }
    }

    public static class Sys {
        public static ErrorType idUknown() {
            return new ErrorType()
                    .withCode(ErrorCodeType.SYS_REQUEST_NOT_PLAUSIBLE)
                    .withNonFatal(false)
                    .withSystemMessage("System ID is unknown");
        }
        public static ErrorType backendFailed(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.SYS_BACKEND_FAILED, systemMsg, userMsg);
        }
        public static ErrorType unknownFailure(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.SYS_UNKNOWN_FAILURE, systemMsg, userMsg);
        }
        public static ErrorType notImplemented(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.SYS_NOT_IMPLEMENTED, systemMsg, userMsg);
        }
        public static ErrorType invalidRequest(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.SYS_REQUEST_NOT_PLAUSIBLE, systemMsg, userMsg);
        }
    }

    public static class Booking {
        public static ErrorType targetUnknown(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.BOOKING_TARGET_UNKNOWN, systemMsg, userMsg);
        }
        public static ErrorType tooShort(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.BOOKING_TOO_SHORT, systemMsg, userMsg);
        }
        public static ErrorType tooLong(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.BOOKING_TOO_LONG, systemMsg, userMsg);
        }
        public static ErrorType targetNotAvail(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.BOOKING_TARGET_NOT_AVAILABLE, systemMsg, userMsg);
        }
        public static ErrorType changeNotPossible(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.BOOKING_CHANGE_NOT_POSSIBLE, systemMsg, userMsg);
        }
        public static ErrorType idUnknown(String systemMsg, String userMsg) {
            return setFields(ErrorCodeType.BOOKING_ID_UNKNOWN, systemMsg, userMsg);
        }
    }

    public static ErrorType noPriceInfo(String systemMsg, String userMsg) {
        return setFields(ErrorCodeType.PRICE_INFO_NOT_AVAILABLE, systemMsg, userMsg);
    }

    public static ErrorType unknownLang(String systemMsg, String userMsg) {
        return setFields(ErrorCodeType.LANGUAGE_NOT_SUPPORTED, systemMsg, userMsg);
    }

    public static ErrorType buildFromException(IxsiCodeException e) {
        return setFields(e.getErrorCode(), e.getMessage(), e.getMessage());
    }

    private static ErrorType setFields(ErrorCodeType c, String systemMsg, String userMsg) {
        return new ErrorType()
                .withCode(c)
                .withNonFatal(true)
                .withSystemMessage(systemMsg)
                .withUserMessage(userMsg);
    }
}
