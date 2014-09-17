package de.rwth.idsg.bikeman.psinterface;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 17.09.2014
 */
public final class Utils {
    private Utils() {}

    public static String getFrom(HttpServletRequest request) {
        return request.getServerName() + ":" + request.getServerPort();
    }
}