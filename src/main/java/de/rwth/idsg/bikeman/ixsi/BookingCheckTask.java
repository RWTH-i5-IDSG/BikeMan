package de.rwth.idsg.bikeman.ixsi;

import de.rwth.idsg.bikeman.ixsi.service.BookingCheckService;
import lombok.RequiredArgsConstructor;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2015
 */
@RequiredArgsConstructor
public class BookingCheckTask implements Runnable {
    private final String ixsiBookingId;
    private final BookingCheckService bookingCheckService;
    private final Runnable task;

    @Override
    public void run() {
        task.run();
        bookingCheckService.completed(ixsiBookingId);
    }
}
