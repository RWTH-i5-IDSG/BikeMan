package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.ViewBookingDTO;
import de.rwth.idsg.bikeman.app.dto.ViewPedelecSlotDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Reservation;
import de.rwth.idsg.bikeman.domain.ReservationState;
import de.rwth.idsg.bikeman.psinterface.dto.request.CancelReservationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.ReserveNowDTO;
import de.rwth.idsg.bikeman.psinterface.rest.client.StationClient;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.repository.ReservationRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class AppBookingService {

    @Autowired
    private AppPedelecService appPedelecService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private StationClient stationClient;

    // TODO: It's not that easy, because you've to inform IXSI! There is still NO solution!
    @Transactional(rollbackFor = Exception.class)
    public Optional<ViewBookingDTO> create(Long stationId, Customer customer) {
        Optional<Reservation> optional = this.getReservation(customer);

        // allow only one reservation at the same time
        if (optional.isPresent()) {
            throw new AppException("Maximum number of concurrent reservations exceeded!", AppErrorCode.BOOKING_BLOCKED);
        }

        Optional<Pedelec> optionalPedelec = appPedelecService.getRecommendedPedelecForBooking(stationId);

        if (!optionalPedelec.isPresent()) {
            return Optional.empty();
        }

        Pedelec pedelec = optionalPedelec.get();

        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(15);

        CardAccount cardAccount = customer.getCardAccount();

        if (cardAccount.getOperationState() != OperationState.OPERATIVE) {
            throw new AppException("The user cannot initiate any booking action", AppErrorCode.BOOKING_BLOCKED);
        }

        if (cardAccount.getInTransaction()) {
            throw new AppException("The user is already in a transaction", AppErrorCode.BOOKING_BLOCKED);
        }

        Reservation reservation = new Reservation();
        reservation.setCardAccount(cardAccount);
        reservation.setStartDateTime(begin);
        reservation.setEndDateTime(end);
        reservation.setState(ReservationState.CREATED);
        reservation.setPedelec(pedelec);

        Reservation savedReservation = reservationRepository.save(reservation);

        // send reservation to station
        String endpointAddress = pedelec.getStationSlot().getStation().getEndpointAddress();
        ReserveNowDTO reserveNowDTO = new ReserveNowDTO(pedelec.getManufacturerId(), cardAccount.getCardId(), end.toDateTime().getMillis());
        reserveNow(endpointAddress, reserveNowDTO);

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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public Optional<Long> getBookingSlotId(Customer customer) {
        Optional<Reservation> optional = this.getReservation(customer);

        if (!optional.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(optional.get().getPedelec().getStationSlot().getStationSlotId());
    }

    @Transactional
    public void delete(Customer customer) {
        Optional<Reservation> optional = this.getReservation(customer);

        if (!optional.isPresent()) {
            throw new AppException("No valid Reservation found!", AppErrorCode.CONSTRAINT_FAILED);
        }

        Reservation reservation = optional.get();

        String endpointAddress = reservation.getPedelec().getStationSlot().getStation().getEndpointAddress();
        CancelReservationDTO cancelReservationDTO = new CancelReservationDTO(reservation.getPedelec().getManufacturerId());
        cancelReservation(endpointAddress, cancelReservationDTO);

        bookingRepository.cancel(reservation.getBooking());
    }

    @Transactional(readOnly = true)
    private Optional<Reservation> getReservation(Customer customer) {
        List<Reservation> reservations = reservationRepository.findByCustomerIdAndTime(
                customer.getCardAccount().getCardAccountId(),
                LocalDateTime.now());

        if (reservations.isEmpty()) {
            return Optional.empty();
        }

        if (reservations.size() > 1) {
            throw new AppException("More than one reservation found!", AppErrorCode.CONSTRAINT_FAILED);
        }

        return Optional.of(reservations.get(0));
    }


    @Async
    public void reserveNow(String endpointAddress, ReserveNowDTO reserveNowDTO) throws DatabaseException {

        stationClient.reserveNow(reserveNowDTO, endpointAddress);
    }

    @Async
    public void cancelReservation(String endpointAddress, CancelReservationDTO cancelReservationDTO)
            throws DatabaseException {

        stationClient.cancelReservation(cancelReservationDTO, endpointAddress);
    }

}
