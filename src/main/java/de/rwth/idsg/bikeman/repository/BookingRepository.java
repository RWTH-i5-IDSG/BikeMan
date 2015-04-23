package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.Transaction;

import java.util.List;

/**
 * Created by max on 24/11/14.
 */
public interface BookingRepository {
    long saveAndGetId(Booking booking);
    Booking updateIxsiBookingId(String ixsiBookingId, Booking booking);
    Booking findByTransaction(Transaction transaction);
    Long findIdByTransaction(Transaction transaction);
    List<Booking> findClosedBookings(List<Long> bookingIdList);
    Booking findOne(Long id);
}
