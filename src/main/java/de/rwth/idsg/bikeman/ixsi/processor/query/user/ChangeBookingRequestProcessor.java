package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.BookingType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeBookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeBookingResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.TimePeriodType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import de.rwth.idsg.bikeman.ixsi.service.AvailabilityPushService;
import de.rwth.idsg.bikeman.ixsi.service.BookingService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class ChangeBookingRequestProcessor implements
        UserRequestProcessor<ChangeBookingRequestType, ChangeBookingResponseType> {

    @Autowired private BookingService bookingService;
    @Autowired private AvailabilityPushService availabilityPushService;

    @Override
    public ChangeBookingResponseType processAnonymously(ChangeBookingRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.Auth.notAnonym("Anonymous change booking request not allowed", null));
    }

    @Override
    public ChangeBookingResponseType processForUser(ChangeBookingRequestType request, Optional<Language> lan,
                                                    UserInfoType userInfo) {
        try {
            if (request.isSetCancel() && request.isCancel()) {
                return proceedCancel(request);

            } else {
                return proceedChange(request);
            }
        } catch (DatabaseException e) {
            return buildError(ErrorFactory.Booking.idUnknown(e.getMessage(), null));

        } catch (IxsiProcessingException e) {
            return buildError(ErrorFactory.Booking.changeNotPossible(e.getMessage(), e.getMessage()));
        }
    }

    private ChangeBookingResponseType proceedChange(ChangeBookingRequestType request) {
        Booking oldBooking = bookingService.get(request.getBookingID());
        TimePeriodType oldTimePeriod = buildTimePeriod(oldBooking);

        Booking newBooking = bookingService.update(oldBooking, request.getNewTimePeriodProposal());
        TimePeriodType newTimePeriod = buildTimePeriod(newBooking);

        BookingType responseBooking = new BookingType()
            .withID(newBooking.getIxsiBookingId())
            .withTimePeriod(newTimePeriod);

        availabilityPushService.changedBooking(request.getBookingID(), oldTimePeriod, newTimePeriod);
        return new ChangeBookingResponseType().withBooking(responseBooking);
    }

    private ChangeBookingResponseType proceedCancel(ChangeBookingRequestType request) {
        Booking booking = bookingService.get(request.getBookingID());
        bookingService.cancel(booking);

        TimePeriodType timePeriod = buildTimePeriod(booking);

        availabilityPushService.cancelledBooking(request.getBookingID(), timePeriod);
        return new ChangeBookingResponseType();
    }

    private TimePeriodType buildTimePeriod(Booking booking) {
        return new TimePeriodType()
            .withBegin(booking.getReservation().getStartDateTime().toDateTime())
            .withEnd(booking.getReservation().getEndDateTime().toDateTime());
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public ChangeBookingResponseType buildError(ErrorType e) {
        return new ChangeBookingResponseType().withError(e);
    }
}
