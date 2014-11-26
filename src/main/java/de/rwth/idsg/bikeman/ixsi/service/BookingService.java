package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Reservation;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.repository.ReservationRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.Duration;
import java.util.Date;
import java.util.List;

/**
 * Created by max on 24/11/14.
 */
@Service
@Slf4j
public class BookingService {

    @Autowired BookingRepository bookingRepository;
    @Autowired ReservationRepository reservationRepository;
    @Autowired CardAccountRepository cardAccountRepository;
    @Autowired PedelecRepository pedelecRepository;

    public long createBookingForUser(String bookeeId, String cardId, Duration reservationDuration) throws DatabaseException {
        // TODO time period??

        // get pedelecId
        Pedelec pedelec = pedelecRepository.findByManufacturerId(bookeeId);

        if (!checkPedelecAvailability(pedelec)) {
            return 0;
        }

        CardAccount cardAccount = cardAccountRepository.findByCardId(cardId);

        LocalDateTime start = LocalDateTime.now();
        // check if millis is small enough
        // TODO how to handle wrong durations?
        Integer delta = safeLongToInt(reservationDuration.getTimeInMillis(new Date()));
        LocalDateTime end = start.plusMillis(delta);

        // check for existing reservation in time frame
        List<Reservation> existingReservations = reservationRepository.findByTimeFrameForPedelec(pedelec.getPedelecId(), start, end);
        if (existingReservations != null && !existingReservations.isEmpty()) {
            return 0;
        }

        Reservation reservation = Reservation.builder()
                .cardAccount(cardAccount)
                .startDateTime(start)
                .endDateTime(end)
                .pedelec(pedelec)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        Booking booking = new Booking();
        booking.setReservation(savedReservation);

        try {
            Booking savedBooking = bookingRepository.save(booking);
            return savedBooking.getBookingId();
        } catch (Throwable e) {
            // TODO
            e.printStackTrace();
            return 0;
        }
    }

    private int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    private boolean checkPedelecAvailability(Pedelec pedelec) {
        if (pedelec.getState() != OperationState.OPERATIVE)
            return false;

        // TODO further checks?

        return true;
    }

}
