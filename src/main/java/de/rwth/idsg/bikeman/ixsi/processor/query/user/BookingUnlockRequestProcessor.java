package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.BookingUnlockRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingUnlockResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import de.rwth.idsg.bikeman.psinterface.rest.client.StationClient;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2014
 */
@Slf4j
@Component
public class BookingUnlockRequestProcessor implements
    UserRequestProcessor<BookingUnlockRequestType, BookingUnlockResponseType> {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private StationClient stationClient;

    @Override
    public BookingUnlockResponseType processAnonymously(BookingUnlockRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.Auth.notAnonym("Anonymous booking unlock request not allowed", null));
    }

    @Override
    public BookingUnlockResponseType processForUser(BookingUnlockRequestType request, Optional<Language> lan,
                                                    UserInfoType userInfo) {
        try {
            Booking booking = bookingRepository.findByIxsiBookingId(request.getBookingID());
            Pedelec pedelec = booking.getReservation().getPedelec();
            Integer stationSlotPosition = pedelec.getStationSlot().getStationSlotPosition();
            String endpointAddress = pedelec.getStationSlot().getStation().getEndpointAddress();

            stationClient.unlockSlot(stationSlotPosition, endpointAddress);
            return new BookingUnlockResponseType();

        } catch (DatabaseException e) {
            return buildError(ErrorFactory.Sys.invalidRequest(e.getMessage(), null));

        } catch (Exception e) {
            return buildError(ErrorFactory.Sys.backendFailed(e.getMessage(), null));
        }
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingUnlockResponseType buildError(ErrorType e) {
        return new BookingUnlockResponseType().withError(e);
    }
}
