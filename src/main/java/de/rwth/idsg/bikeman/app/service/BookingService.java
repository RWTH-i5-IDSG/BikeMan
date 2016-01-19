package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.ViewBookingDTO;
import de.rwth.idsg.bikeman.app.dto.ViewPedelecSlotDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service("BookingServiceApp")
@Slf4j
public class BookingService {

    @Autowired
    private PedelecService pedelecService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public Optional<ViewBookingDTO> create(Long stationId, Customer customer) {
        Optional<Reservation> optional = this.getReservation(customer);

        // allow only one reservation at the same time
        if (!optional.isPresent()) {
            throw new AppException("Maximum number of concurrent reservations exceeded!", AppErrorCode.BOOKING_BLOCKED);
        }

        Pedelec pedelec = pedelecService.getRecommendedPedelecForBooking(stationId);

        if (pedelec == null) {
            return Optional.empty();
        }

        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(15);

        Reservation reservation = new Reservation();
        reservation.setCardAccount(customer.getCardAccount());
        reservation.setStartDateTime(begin);
        reservation.setEndDateTime(end);
        reservation.setState(ReservationState.CREATED);
        reservation.setPedelec(pedelec);

        Reservation savedReservation = reservationRepository.save(reservation);

        Booking booking = new Booking();
        booking.setReservation(savedReservation);
        try {
            bookingRepository.save(booking);
            return Optional.of(
                    ViewBookingDTO.builder()
                        .expiryDateTime(end)
                        .stationSlotPosition(pedelec.getStationSlot().getStationSlotPosition())
                        .stationId(stationId)
                        .build()
                    );
        } catch (Throwable e) {
            throw new AppException("Failed during database operation.", AppErrorCode.DATABASE_OPERATION_FAILED);
        }
    }

    @Transactional(readOnly=true)
    public Optional<ViewBookingDTO> getDTO(Customer customer) {
        Optional<Reservation> optional = this.getReservation(customer);

        if (!optional.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(
                ViewBookingDTO.builder()
                    .stationId(optional.get().getPedelec().getStationSlot().getStation().getStationId())
                    .stationSlotPosition(optional.get().getPedelec().getStationSlot().getStationSlotPosition())
                    .expiryDateTime(optional.get().getEndDateTime())
                    .build()
                );

    }

    @Transactional(readOnly=true)
    public Optional<ViewPedelecSlotDTO> getSlot(Customer customer) {
        Optional<Reservation> optional = this.getReservation(customer);

        if (!optional.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(
                ViewPedelecSlotDTO.builder()
                    .stationSlotId(optional.get().getPedelec().getStationSlot().getStationSlotId())
                    .stationSlotPosition(optional.get().getPedelec().getStationSlot().getStationSlotPosition())
                    .build()
                );
    }

    @Transactional(readOnly=true)
    public Optional<Long> getBookingSlotId(Customer customer) {
        Optional<Reservation> optional = this.getReservation(customer);

        if (!optional.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(optional.get().getPedelec().getStationSlot().getStationSlotId());
    }

    @Transactional(readOnly=false)
    public void delete(Customer customer) {
        Optional<Reservation> optional = this.getReservation(customer);

        if (!optional.isPresent()) {
            throw new AppException("No valid Reservation found!", AppErrorCode.CONSTRAINT_FAILED);
        }

        reservationRepository.updateEndDateTime(optional.get().getReservationId(), LocalDateTime.now());
    }

    @Transactional(readOnly=true)
    private Optional<Reservation> getReservation(Customer customer) {
        List<Reservation> reservations = reservationRepository.findByCustomerIdAndTime(
            customer.getCardAccount().getCardAccountId(),
            LocalDateTime.now());

        if (reservations.isEmpty()) {
            return Optional.empty();
        }

        if (reservations.size() > 1) {
            throw new AppException("More than one Reservations found!", AppErrorCode.CONSTRAINT_FAILED);
        }

        return Optional.of(reservations.get(0));
    }

}
