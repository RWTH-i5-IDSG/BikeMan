package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IxsiCodeException;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.service.AvailabilityPushService;
import de.rwth.idsg.bikeman.ixsi.service.BookingCheckService;
import de.rwth.idsg.bikeman.ixsi.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.BookingRequestType;
import xjc.schema.ixsi.BookingResponseType;
import xjc.schema.ixsi.BookingType;
import xjc.schema.ixsi.ErrorType;
import xjc.schema.ixsi.Language;
import xjc.schema.ixsi.TimePeriodType;
import xjc.schema.ixsi.UserInfoType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingRequestProcessor implements
        UserRequestProcessor<BookingRequestType, BookingResponseType> {

    @Autowired private BookingService bookingService;
    @Autowired private AvailabilityPushService availabilityPushService;
    @Autowired private BookingCheckService bookingCheckService;

    @Override
    public Class<BookingRequestType> getProcessingClass() {
        return BookingRequestType.class;
    }

    @Override
    public BookingResponseType processAnonymously(BookingRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.Auth.notAnonym("Anonymous booking makes no sense", null));
    }

    @Override
    public BookingResponseType processForUser(BookingRequestType request, Optional<Language> lan,
                                              UserInfoType userInfo) {
        try {
            Booking createdBooking = bookingService.createBookingForUser(
                request.getBookingTargetID().getBookeeID(),
                userInfo.getUserID(),
                request.getTimePeriodProposal()
            );

            TimePeriodType timePeriod = new TimePeriodType()
                .withBegin(createdBooking.getReservation().getStartDateTime().toDateTime())
                .withEnd(createdBooking.getReservation().getEndDateTime().toDateTime());

            String placeId = createdBooking.getReservation()
                                           .getPedelec()
                                           .getStationSlot()
                                           .getStation()
                                           .getManufacturerId();

            availabilityPushService.placedBooking(request.getBookingTargetID().getBookeeID(), placeId, timePeriod);
            bookingCheckService.placedBooking(createdBooking);

            BookingType booking = new BookingType()
                .withID(String.valueOf(createdBooking.getIxsiBookingId()))
                .withTimePeriod(timePeriod);

            return new BookingResponseType().withBooking(booking);

        } catch (IxsiCodeException e) {
            return buildError(ErrorFactory.buildFromException(e));

        } catch (IxsiProcessingException e) {
            return buildError(ErrorFactory.Booking.targetNotAvail(e.getMessage(), e.getMessage()));

        } catch (Exception e) {
            return buildError(ErrorFactory.Sys.backendFailed(e.getMessage(), "Booking not possible due to backend error"));
        }
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingResponseType buildError(ErrorType e) {
        return new BookingResponseType().withError(e);
    }
}
