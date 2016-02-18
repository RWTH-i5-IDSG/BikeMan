package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Reservation;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.IxsiCodeException;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.psinterface.dto.request.CancelReservationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.ReserveNowDTO;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.repository.ReservationRepository;
import de.rwth.idsg.bikeman.service.StationService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xjc.schema.ixsi.ErrorCodeType;
import xjc.schema.ixsi.TimePeriodProposalType;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired private StationService stationService;

    private static final int BOOKING_MIN_TIME_WINDOW_IN_MIN = 15;
    private static final int BOOKING_MAX_TIME_WINDOW_IN_MIN = 60;

    @Transactional
    public Booking createBookingForUser(String bookeeId, String cardId, TimePeriodProposalType timePeriodProposal)
        throws DatabaseException {

        LocalDateTime begin = new LocalDateTime();
        LocalDateTime end = begin.plus(getBookingDuration(timePeriodProposal));

        //checkTimeFrameForSanity(begin, end);

        if (timePeriodProposal.isSetMaxWait()) {
            // TODO Incorporate maxWait into processing.
            // Not sure about the approach: range search between begin and begin + maxWait for existing reservations?
            Period maxWait = timePeriodProposal.getMaxWait();
        }

        Pedelec pedelec = pedelecRepository.findByManufacturerId(bookeeId);
        check(pedelec);

        CardAccount cardAccount = cardAccountRepository.findByCardId(cardId);
        check(cardAccount);

        // check for existing reservation in time frame
        List<Reservation> existingReservations = reservationRepository.findByTimeFrameForPedelec(pedelec.getPedelecId(), begin, end);
        if (existingReservations != null && !existingReservations.isEmpty()) {
            throw new IxsiProcessingException("There is an overlapping booking for the target in this time period");
        }

        Reservation reservation = new Reservation();
        reservation.setCardAccount(cardAccount);
        reservation.setStartDateTime(begin);
        reservation.setEndDateTime(end);
        reservation.setPedelec(pedelec);

        Reservation savedReservation = reservationRepository.save(reservation);

        // send reservation to station
        String endpointAddress = pedelec.getStationSlot().getStation().getEndpointAddress();
        ReserveNowDTO reserveNowDTO = new ReserveNowDTO(pedelec.getManufacturerId(), cardAccount.getCardId(), end.toDateTime());
        stationService.reserveNow(endpointAddress, reserveNowDTO);

        Booking booking = new Booking();
        booking.setReservation(savedReservation);
        try {
            return bookingRepository.save(booking);
        } catch (Throwable e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    public Booking get(String bookingId, String userId) {
        Booking b = bookingRepository.findByIxsiBookingIdForUser(bookingId, userId);
        checkState(b.getReservation().getCardAccount());
        return b;
    }

    /**
     * @return Cancelled booking
     */
    @Transactional
    public Booking cancel(String bookingId, String userId) {
        Booking booking = this.get(bookingId, userId);

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

        // send 'cancelReservation' to station
        String endpointAddress = reservation.getPedelec().getStationSlot().getStation().getEndpointAddress();
        CancelReservationDTO cancelReservationDTO = new CancelReservationDTO(reservation.getPedelec().getManufacturerId());
        stationService.cancelReservation(endpointAddress, cancelReservationDTO);

        return booking;
    }

    @Transactional
    public Booking update(Booking booking, TimePeriodProposalType newTimePeriodProposal) {
        Reservation reservation = booking.getReservation();

        LocalDateTime begin = new LocalDateTime();
        LocalDateTime end = begin.plus(getBookingDuration(newTimePeriodProposal));

        // check for new time period validity
        // TODO introduce max/min
        //checkTimeFrameForSanity(begin, end);

        List<Reservation> existingReservations = reservationRepository.findOverlappingReservations(
            reservation.getPedelec().getPedelecId(), reservation.getReservationId(), begin, end);
        if (!existingReservations.isEmpty()) {
            throw new IxsiProcessingException("Proposed time period overlaps existing booking.");
        }

        reservation.setStartDateTime(begin);
        reservation.setEndDateTime(end);
        reservationRepository.save(reservation);

        Pedelec pedelec = reservation.getPedelec();
        CardAccount cardAccount = reservation.getCardAccount();

        // send an updated reserve-now to station with new time
        String endpointAddress = pedelec.getStationSlot().getStation().getEndpointAddress();
        ReserveNowDTO reserveNowDTO = new ReserveNowDTO(pedelec.getManufacturerId(), cardAccount.getCardId(), end.toDateTime());
        stationService.reserveNow(endpointAddress, reserveNowDTO);

        return booking;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * In this context, the actual IXSI begin and end timestamps are not important,
     * since BikeMan use case only defines to book pedelecs for a specified period of time
     * starting with the present time.
     *
     * Therefore we only extract the duration between them.
     */
    private Duration getBookingDuration(TimePeriodProposalType tp) {
        Duration duration = new Duration(tp.getBegin(), tp.getEnd());
        long millis = duration.getMillis();

        boolean tooShort = millis < TimeUnit.MINUTES.toMillis(BOOKING_MIN_TIME_WINDOW_IN_MIN);
        if (tooShort) {
            throw new IxsiCodeException(
                "Desired booking time window is too short. Must be at least " + BOOKING_MIN_TIME_WINDOW_IN_MIN + " minutes",
                ErrorCodeType.BOOKING_TOO_SHORT);
        }

        boolean tooLong = millis > TimeUnit.MINUTES.toMillis(BOOKING_MAX_TIME_WINDOW_IN_MIN);
        if (tooLong) {
            throw new IxsiCodeException(
                "Desired booking time window is too long. Must be at most " + BOOKING_MAX_TIME_WINDOW_IN_MIN + " minutes",
                ErrorCodeType.BOOKING_TOO_LONG);
        }

        return duration;
    }

    /**
     * TODO: What is a reasonable value for lowerLimit? Is SoC check a good solution?
     */
    private void check(Pedelec pedelec) {
        final double lowerLimit = 0.0;

        boolean isAvailable =  OperationState.OPERATIVE.equals(pedelec.getState())
            && !pedelec.getInTransaction()
            && pedelec.getChargingStatus().getBatteryStateOfCharge() > lowerLimit;

        if (!isAvailable) {
            throw new IxsiProcessingException("The booking target is not available.");
        }
    }

    private void check(CardAccount ca) {
        checkState(ca);

        if (ca.getInTransaction()) {
            throw new IxsiCodeException("The user is already in a transaction", ErrorCodeType.SYS_REQUEST_NOT_PLAUSIBLE);
        }
    }

    private void checkState(CardAccount ca) {
        if (ca.getOperationState() != OperationState.OPERATIVE) {
            throw new IxsiCodeException("The user cannot initiate any booking action", ErrorCodeType.SYS_REQUEST_NOT_PLAUSIBLE);
        }
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
