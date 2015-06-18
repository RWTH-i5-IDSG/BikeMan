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

        String remoteAddress = request.getHeader("X-Real-IP");

        if (remoteAddress == null) {
            remoteAddress = request.getRemoteAddr();
        }

        //return request.getScheme() + "://" + remoteAddress + ":" + request.getRemotePort();
        return request.getScheme() + "://" + remoteAddress;
    }

    public static long toSeconds(long millis) {
        return TimeUnit.MILLISECONDS.toSeconds(millis);
    }

    public static long toMillis(long seconds) {
        return TimeUnit.SECONDS.toMillis(seconds);
    }

    public static long nowInSeconds() {
        return toSeconds(new DateTime().getMillis());
    }
}
