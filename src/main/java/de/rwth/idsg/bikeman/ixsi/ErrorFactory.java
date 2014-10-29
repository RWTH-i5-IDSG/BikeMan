package de.rwth.idsg.bikeman.ixsi;

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
        e.setCode("");
        e.setSystemMessage("");
        e.setUserMessage("");
        return e;
    }

    public static ErrorType invalidSystem() {
        ErrorType e = new ErrorType();
        e.setNonFatal(false);
        e.setCode("");
        e.setSystemMessage("");
        e.setUserMessage("");
        return e;
    }

    public static ErrorType invalidUserAuth() {
        ErrorType e = new ErrorType();
        e.setNonFatal(false);
        e.setCode("");
        e.setSystemMessage("");
        e.setUserMessage("");
        return e;
    }
}