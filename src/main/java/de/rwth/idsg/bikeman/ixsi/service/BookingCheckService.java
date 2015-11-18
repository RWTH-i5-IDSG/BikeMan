package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.ixsi.BookingCheckTask;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * We should identify reservations, that have been not used (i.e. no transaction started within the reservation
 * time period), and send (1) booking alert notification and (2) empty consumption to the IXSI server.
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2015
 */
@Slf4j
@Service
public class BookingCheckService {

    @Autowired private ScheduledExecutorService executorService;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private ConsumptionPushService consumptionPushService;
    @Autowired private BookingAlertPushService bookingAlertPushService;

    // Key : ixsiBookingId
    private final ConcurrentHashMap<String, ScheduledFuture> lookupTable = new ConcurrentHashMap<>();

    // Let's not check exactly at the end of the reservation, but some time later
    public static final int BUFFER_IN_MIN = 2;

    public void placedBooking(Booking booking) {
        String ixsiBookingId = booking.getIxsiBookingId();

        LocalDateTime end = booking.getReservation().getEndDateTime();
        DateTime reservationEnd = new DateTime(end);

        BookingCheckTask c = new BookingCheckTask(ixsiBookingId, this, () -> {
           boolean notUsed = bookingRepository.isNotUsedAndExpired(ixsiBookingId);
            if (notUsed) {
                bookingAlertPushService.alertNotUsed(ixsiBookingId);
                consumptionPushService.reportNotUsed(ixsiBookingId, reservationEnd);
            }
        });

        long endTimestamp = reservationEnd.plusMinutes(BUFFER_IN_MIN).getMillis();
        ScheduledFuture ff = executorService.schedule(c, endTimestamp, TimeUnit.MILLISECONDS);
        lookupTable.put(booking.getIxsiBookingId(), ff);
    }

    public void changedBooking(Booking oldBooking, Booking newBooking) {
        cancelledBooking(oldBooking);
        placedBooking(newBooking);
    }

    public void cancelledBooking(Booking booking) {
        ScheduledFuture f = lookupTable.remove(booking.getIxsiBookingId());
        if (f != null) {
            f.cancel(true);
        }
    }

    public void completed(String ixsiBookingId) {
        lookupTable.remove(ixsiBookingId);
    }
}
