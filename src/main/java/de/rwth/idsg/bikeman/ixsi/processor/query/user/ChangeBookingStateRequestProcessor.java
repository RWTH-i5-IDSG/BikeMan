package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.service.BookingService;
import de.rwth.idsg.bikeman.psinterface.dto.request.RemoteAuthorizeDTO;
import de.rwth.idsg.bikeman.psinterface.rest.client.StationClient;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.ChangeBookingStateRequestType;
import xjc.schema.ixsi.ChangeBookingStateResponseType;
import xjc.schema.ixsi.ErrorType;
import xjc.schema.ixsi.Language;
import xjc.schema.ixsi.UserInfoType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2014
 */
@Slf4j
@Component
public class ChangeBookingStateRequestProcessor implements
    UserRequestProcessor<ChangeBookingStateRequestType, ChangeBookingStateResponseType> {

    @Autowired private BookingService bookingService;
    @Autowired private StationClient stationClient;

    @Override
    public Class<ChangeBookingStateRequestType> getProcessingClass() {
        return ChangeBookingStateRequestType.class;
    }

    @Override
    public ChangeBookingStateResponseType processAnonymously(ChangeBookingStateRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.Auth.notAnonym("Anonymous change booking state request not allowed", null));
    }

    @Override
    public ChangeBookingStateResponseType processForUser(ChangeBookingStateRequestType request, Optional<Language> lan,
                                                         UserInfoType userInfo) {
        try {
            switch (request.getBookingState()) {
                case OPEN:
                    return proceedOpen(request, userInfo);
                case CLOSED:
                    return buildError(ErrorFactory.Sys.notImplemented(null, null));
                case SUSPENDED:
                    return buildError(ErrorFactory.Sys.notImplemented(null, null));
                default:
                    return buildError(ErrorFactory.Sys.invalidRequest("BookingState is missing", null));
            }
        } catch (DatabaseException e) {
            return buildError(ErrorFactory.Sys.invalidRequest(e.getMessage(), null));

        } catch (IxsiProcessingException e) {
            return buildError(ErrorFactory.Booking.changeNotPossible(e.getMessage(), e.getMessage()));
        }
    }

    private ChangeBookingStateResponseType proceedOpen(ChangeBookingStateRequestType request,
                                                       UserInfoType userInfo) {
        Booking booking = bookingService.get(request.getBookingID(), userInfo.getUserID());

        Pedelec pedelec = booking.getReservation().getPedelec();
        Integer stationSlotPosition = pedelec.getStationSlot().getStationSlotPosition();
        String endpointAddress = pedelec.getStationSlot().getStation().getEndpointAddress();

        RemoteAuthorizeDTO dto = RemoteAuthorizeDTO.builder()
                                                   .cardId(userInfo.getUserID())
                                                   .slotPosition(stationSlotPosition)
                                                   .build();

        // throws PsException
        stationClient.authorizeRemote(endpointAddress, dto);
        return new ChangeBookingStateResponseType();
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public ChangeBookingStateResponseType buildError(ErrorType e) {
        return new ChangeBookingStateResponseType().withError(e);
    }
}
