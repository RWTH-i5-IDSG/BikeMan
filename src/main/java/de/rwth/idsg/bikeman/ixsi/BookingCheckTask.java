package de.rwth.idsg.bikeman.ixsi;

import de.rwth.idsg.bikeman.ixsi.service.BookingCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2015
 */
@Slf4j
@RequiredArgsConstructor
public class BookingCheckTask implements Runnable {
    private final BookingCheckService bookingCheckService;
    private final String ixsiBookingId;
    private final DateTime reservationEnd;

    @Override
    public void run() {
        log.debug("Running the booking check");
        bookingCheckService.check(ixsiBookingId, reservationEnd);
    }
}
