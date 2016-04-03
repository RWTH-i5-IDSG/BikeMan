package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.ViewBookingDTO;
import de.rwth.idsg.bikeman.app.dto.ViewPedelecSlotDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Reservation;
import de.rwth.idsg.bikeman.ixsi.IxsiCodeException;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.service.AvailabilityPushService;
import de.rwth.idsg.bikeman.ixsi.service.BookingCheckService;
import de.rwth.idsg.bikeman.ixsi.service.BookingService;
import de.rwth.idsg.bikeman.psinterface.dto.request.CancelReservationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.ReserveNowDTO;
import de.rwth.idsg.bikeman.psinterface.rest.client.StationClient;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.repository.ReservationRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xjc.schema.ixsi.TimePeriodProposalType;
import xjc.schema.ixsi.TimePeriodType;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class AppBookingService {

    @Autowired private AppPedelecService appPedelecService;

    @Autowired private BookingRepository bookingRepository;

    @Autowired private BookingService bookingService;

    @Autowired private ReservationRepository reservationRepository;

    @Autowired private StationClient stationClient;

    @Autowired private AvailabilityPushService availabilityPushService;

    @Autowired private BookingCheckService bookingCheckService;

    @Transactional
    public Optional<ViewBookingDTO> createBooking(Long stationId, Customer customer) {

        try {
            TimePeriodProposalType timePeriodProposal = new TimePeriodProposalType()
                .withBegin(new DateTime())
                .withEnd(new DateTime().plusMinutes(15));

            Optional<Pedelec> optionalPedelec = appPedelecService.getRecommendedPedelecForBooking(stationId);

            if (!optionalPedelec.isPresent()) {
                return Optional.empty();
            }

            Pedelec pedelec = optionalPedelec.get();

            Booking createdBooking = bookingService.createBookingForUser(
                pedelec.getManufacturerId(),
                customer.getCardAccount().getCardId(),
                timePeriodProposal
            );

            TimePeriodType timePeriod = new TimePeriodType()
                .withBegin(createdBooking.getReservation().getStartDateTime().toDateTime())
                .withEnd(createdBooking.getReservation().getEndDateTime().toDateTime());

            String placeId = createdBooking.getReservation()
                .getPedelec()
                .getStationSlot()
                .getStation()
                .getManufacturerId();

            availabilityPushService.placedBooking(pedelec.getManufacturerId(), placeId, timePeriod);
            bookingCheckService.placedBooking(createdBooking);

            ViewBookingDTO viewBookingDTO = ViewBookingDTO.builder()
                .expiryDateTime(new LocalDateTime(timePeriod.getEnd()))
                .stationSlotPosition(pedelec.getStationSlot().getStationSlotPosition())
                .stationId(stationId)
                .build();

            return Optional.of(viewBookingDTO);

        } catch (IxsiCodeException e) {
            return Optional.empty();
        } catch (IxsiProcessingException e) {
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
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

        Booking booking = bookingService.cancel(reservation.getBooking().getIxsiBookingId(), customer.getCardAccount().getCardId());

        TimePeriodType timePeriod = new TimePeriodType()
            .withBegin(booking.getReservation().getStartDateTime().toDateTime())
            .withEnd(booking.getReservation().getEndDateTime().toDateTime());

        String pedelecId = booking.getReservation()
            .getPedelec()
            .getManufacturerId();

        String placeId = booking.getReservation()
            .getPedelec()
            .getStationSlot()
            .getStation()
            .getManufacturerId();

        availabilityPushService.cancelledBooking(pedelecId, placeId, timePeriod);
        bookingCheckService.cancelledBooking(booking);
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

}
