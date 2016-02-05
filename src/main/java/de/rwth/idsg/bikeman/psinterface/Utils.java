package de.rwth.idsg.bikeman.psinterface;

import de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 17.09.2014
 */
public final class Utils {
    private Utils() {}

    public static String getFrom(HttpServletRequest request) {
        return getStationId(request);
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

    public static boolean isEmpty(List array) {
        if (array == null || array.isEmpty()) {
            return true;
        }

        return false;
    }

    private static String getIp(HttpServletRequest request) {
        String remoteAddress = request.getHeader("X-Real-IP");

        if (remoteAddress == null) {
            remoteAddress = request.getRemoteAddr();
        }

        //return request.getScheme() + "://" + remoteAddress + ":" + request.getRemotePort();
        return request.getScheme() + "://" + remoteAddress;
    }

    /**
     * @return The station's manufacturer Id
     */
    private static String getStationId(HttpServletRequest request) {
        String stationId = request.getHeader("STATION-ID");
        if (stationId == null) {
            throw new PsException("A required header is not set", PsErrorCode.CONSTRAINT_FAILED);
        }
        return stationId;
    }
}
