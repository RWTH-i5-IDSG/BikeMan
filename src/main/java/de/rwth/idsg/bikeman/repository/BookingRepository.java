package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.ixsi.dto.BookingDTO;

import java.util.List;

/**
 * Created by max on 24/11/14.
 */
public interface BookingRepository {
    Booking save(Booking booking);
    void cancel(Booking booking);
    boolean isNotUsedAndExpired(String ixsiBookingId);
    List<BookingDTO> findNotUsedAndExpiredBookings(List<String> ixsiBookingIdList);
    List<Booking> findClosedBookings(List<String> ixsiBookingIdList);
    Booking findByIxsiBookingIdForUser(String ixsiBookingId, String userId);
}
