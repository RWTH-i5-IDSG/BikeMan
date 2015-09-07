package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.Reservation;
import de.rwth.idsg.bikeman.domain.Transaction;

import java.util.List;

/**
 * Created by max on 24/11/14.
 */
public interface BookingRepository {
    Booking save(Booking booking);
    void cancel(Booking booking);
    List<Booking> findClosedBookings(List<String> ixsiBookingIdList);
    Booking findByIxsiBookingIdForUser(String ixsiBookingId, String userId);
}
