package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Reservation;
import de.rwth.idsg.bikeman.ixsi.schema.BookingType;
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

    public long createBookingForUser(String bookeeId, String userId, Duration reservationDuration) throws DatabaseException {
        // TODO time period??

        // get pedelecId
        Pedelec pedelec = pedelecRepository.findByManufacturerId(bookeeId);

        CardAccount cardAccount = cardAccountRepository.findByUserLogin(userId).get(0);

        LocalDateTime start = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .cardAccount(cardAccount)
                .startDateTime(start)
                .endDateTime(start.plusMinutes(reservationDuration.getMinutes()))
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

}
