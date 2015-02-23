package de.rwth.idsg.bikeman.psinterface;

import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 17.09.2014
 */
public final class Utils {
    private Utils() {}

    public static String getFrom(HttpServletRequest request) {
        return request.getServerName() + ":" + request.getServerPort();
    }

    public static long toSeconds(long millis) {
        return TimeUnit.MILLISECONDS.toSeconds(millis);
    }

    public static long getSecondsOfNow() {
        return toSeconds(new DateTime().getMillis());
    }
}