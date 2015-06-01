package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.schema.TimePeriodProposalType;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.repository.ReservationRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.datatype.Duration;
import java.util.List;

/**
 * Created by max on 24/11/14.
 */
@Service
@Slf4j
public class BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private CardAccountRepository cardAccountRepository;
    @Autowired private PedelecRepository pedelecRepository;

    @Transactional
    public Booking createBookingForUser(String bookeeId, String cardId, TimePeriodProposalType timePeriodProposal)
        throws DatabaseException {

        LocalDateTime begin = timePeriodProposal.getBegin().toLocalDateTime();
        LocalDateTime end = timePeriodProposal.getEnd().toLocalDateTime();

        checkTimeFrameForSanity(begin, end);

        if (timePeriodProposal.isSetMaxWait()) {
            // TODO Incorporate maxWait into processing.
            // Not sure about the approach: range search between begin and begin + maxWait for existing reservations?
            Duration maxWait = timePeriodProposal.getMaxWait();
        }

        Pedelec pedelec = pedelecRepository.findByManufacturerId(bookeeId);
        if (!isAvailable(pedelec)) {
            throw new IxsiProcessingException("The booking target is not available.");
        }

        CardAccount cardAccount = cardAccountRepository.findByCardId(cardId);

        // check for existing reservation in time frame
        List<Reservation> existingReservations = reservationRepository.findByTimeFrameForPedelec(pedelec.getPedelecId(), begin, end);
        if (existingReservations != null && !existingReservations.isEmpty()) {
            throw new IxsiProcessingException("There is an overlapping booking for the target in this time period");
        }

        Reservation reservation = Reservation.builder()
            .cardAccount(cardAccount)
            .startDateTime(begin)
            .endDateTime(end)
            .pedelec(pedelec)
            .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        Booking booking = new Booking();
        booking.setReservation(savedReservation);
        try {
            return bookingRepository.save(booking);
        } catch (Throwable e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    public void cancel(String bookingId) {
        Booking booking = bookingRepository.findByIxsiBookingId(bookingId);

        Transaction transaction = booking.getTransaction();
        if (transaction != null) {
            throw new IxsiProcessingException("The pedelec is already taken, too late cannot cancel");
        }

        Reservation reservation = booking.getReservation();

        LocalDateTime end = reservation.getEndDateTime();
        LocalDateTime now = new LocalDateTime();
        if (now.isAfter(end)) {
            throw new IxsiProcessingException("The booking is already over, cannot cancel");
        }

        bookingRepository.cancel(booking);
    }

    public Booking update(String bookingId, TimePeriodProposalType newTimePeriodProposal) {
        Booking booking = bookingRepository.findByIxsiBookingId(bookingId);
        Reservation reservation = booking.getReservation();

        LocalDateTime begin = newTimePeriodProposal.getBegin().toLocalDateTime();
        LocalDateTime end = newTimePeriodProposal.getEnd().toLocalDateTime();

        // check for new time period validity
        // TODO introduce max/min
        checkTimeFrameForSanity(begin, end);
        List<Reservation> existingReservations = reservationRepository.findByTimeFrameForPedelec(reservation.getPedelec().getPedelecId(), begin, end);
        if (!existingReservations.isEmpty()) {
            throw new IxsiProcessingException("Proposed time period overlaps existing booking.");
        }

        // perform update in db
        reservationRepository.updateTimeWindow(reservation.getReservationId(), begin, end);

        // get the updated booking
        return bookingRepository.findByIxsiBookingId(bookingId);
    }

    /**
     * TODO: What is a reasonable value for lowerLimit?
     */
    private boolean isAvailable(Pedelec pedelec) {
        final float lowerLimit = 0.0f;

        return OperationState.OPERATIVE.equals(pedelec.getState())
            && !pedelec.getInTransaction()
            && pedelec.getStateOfCharge() > lowerLimit;
    }

    /**
     * TODO: More rules/boundaries are needed for acceptable reservations
     *
     * 1) Min/max allowed duration? (reserve for 1 min / 5 years?)
     * 2) How soon is the begin? (start now / next year?)
     */
    private void checkTimeFrameForSanity(LocalDateTime begin, LocalDateTime end) {
        LocalDateTime now = new LocalDateTime();

        // Continue only if: now < start < end
        if (!(now.isBefore(begin) && begin.isBefore(end))) {
            throw new IxsiProcessingException("Unacceptable date/time values");
        }
    }
}
