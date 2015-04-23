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
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.Duration;
import java.util.List;

/**
 * Created by max on 24/11/14.
 */
@Service
@Slf4j
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CardAccountRepository cardAccountRepository;
    @Autowired
    private PedelecRepository pedelecRepository;

    public long createBookingForUser(String bookeeId, String cardId, TimePeriodProposalType timePeriodProposal)
        throws DatabaseException {

        Pedelec pedelec = pedelecRepository.findByManufacturerId(bookeeId);
        if (!isAvailable(pedelec)) {
            throw new IxsiProcessingException("The booking target is not available.");
        }

        CardAccount cardAccount = cardAccountRepository.findByCardId(cardId);

        LocalDateTime begin = timePeriodProposal.getBegin().toLocalDateTime();
        LocalDateTime end = timePeriodProposal.getEnd().toLocalDateTime();

        if (timePeriodProposal.isSetMaxWait()) {
            // TODO Incorporate maxWait into processing.
            // Not sure about the approach: range search between begin and begin + maxWait for existing reservations?
            Duration maxWait = timePeriodProposal.getMaxWait();
        }

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
            return bookingRepository.save(booking).getBookingId();
        } catch (Throwable e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
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

}
