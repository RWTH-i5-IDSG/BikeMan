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


@Service("BookingServiceApp")
@Slf4j
public class BookingService {

    @Autowired
    private PedelecService pedelecService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ViewBookingDTO create(Long stationId, Customer customer) {
        Reservation existingReservation = reservationRepository.findByCustomerIdAndTime(
            customer.getCardAccount().getCardAccountId(),
            LocalDateTime.now());

        // allow only one reservation at the same time
        if (existingReservation != null) {
            throw new AppException("Maximum number of concurrent reservations exceeded!", AppErrorCode.BOOKING_BLOCKED);
        }

        Pedelec pedelec = pedelecService.getRecommendedPedelecForBooking(stationId);

        if (pedelec == null) {
            return null;
        }

        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(15);

        Reservation reservation = Reservation.builder()
            .cardAccount(customer.getCardAccount())
            .startDateTime(begin)
            .endDateTime(end)
            .pedelec(pedelec)
            .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        Booking booking = new Booking();
        booking.setReservation(savedReservation);
        try {
            bookingRepository.save(booking);
            return ViewBookingDTO.builder()
                        .expiryDateTime(end)
                        .stationSlotPosition(pedelec.getStationSlot().getStationSlotPosition())
                        .stationId(stationId)
                        .build();
        } catch (Throwable e) {
            throw new AppException("Failed during database operation.", AppErrorCode.DATABASE_OPERATION_FAILED);
        }
    }

    public ViewBookingDTO getDTO(Customer customer) {
        Reservation reservation = reservationRepository.findByCustomerIdAndTime(
                        customer.getCardAccount().getCardAccountId(),
                        LocalDateTime.now());

        if (reservation == null) {
            return null;
        }

        return ViewBookingDTO.builder()
                .stationId(reservation.getPedelec().getStationSlot().getStation().getStationId())
                .stationSlotPosition(reservation.getPedelec().getStationSlot().getStationSlotPosition())
                .expiryDateTime(reservation.getEndDateTime())
                .build();

    }

    @Transactional(readOnly=true)
    public ViewPedelecSlotDTO getSlot(Customer customer) {
        Reservation reservation = reservationRepository.findByCustomerIdAndTime(
            customer.getCardAccount().getCardAccountId(),
            LocalDateTime.now());

        if (reservation == null) {
            return null;
        }

        return ViewPedelecSlotDTO.builder()
            .stationSlotId(reservation.getPedelec().getStationSlot().getStationSlotId())
            .stationSlotPosition(reservation.getPedelec().getStationSlot().getStationSlotPosition())
            .build();
    }

    @Transactional(readOnly = false)
    public void delete(Customer customer) {
        Reservation reservation = reservationRepository.findByCustomerIdAndTime(
            customer.getCardAccount().getCardAccountId(),
            LocalDateTime.now());

        if (reservation == null) {
            throw new AppException("No valid Reservation found!", AppErrorCode.CONSTRAINT_FAILED);
        }

        reservationRepository.updateEndDateTime(reservation.getReservationId(), LocalDateTime.now());
    }

}