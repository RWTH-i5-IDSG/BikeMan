package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.processor.UserValidator;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.*;
import de.rwth.idsg.bikeman.ixsi.service.BookingService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingRequestProcessor implements
        UserRequestProcessor<BookingRequestType, BookingResponseType> {

    @Autowired private BookingService bookingService;
    @Autowired private UserValidator userValidator;

    @Override
    public BookingResponseType processAnonymously(BookingRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.invalidRequest("Anonymous booking makes no sense", null));
    }

    /**
     * This method has to validate the user infos !!!!
     */
    @Override
    public BookingResponseType processForUser(BookingRequestType request, Optional<Language> lan,
                                              List<UserInfoType> userInfoList) {
        BookingResponseType bookingResponse = new BookingResponseType();

        // validate user auth token
        UserValidator.Results results = userValidator.validate(userInfoList);

        List<ErrorType> errors = results.getErrors();
        if (!errors.isEmpty()) {
            bookingResponse.getError().addAll(errors);
        }

        List<UserInfoType> validUsers = results.getValidUsers();
        if (validUsers.size() != 1) {
            return buildError(ErrorFactory.invalidRequest("Invalid Request", "Not more than one user allowed per request"));
        }
        UserInfoType user = validUsers.get(0);

        // validate that user has the right to reserve for given time period
        if (!request.isSetBookingTargetID() || !request.isSetTimePeriodProposal()) {
            return buildError(ErrorFactory.invalidRequest("Invalid Parameters", "Invalid Parameters"));
        }

        try {
            Booking createdBooking = bookingService.createBookingForUser(request.getBookingTargetID().getBookeeID(),
                                                                 user.getUserID(),
                                                                 request.getTimePeriodProposal());

            TimePeriodType timePeriod = new TimePeriodType()
                .withBegin(createdBooking.getReservation().getStartDateTime().toDateTime())
                .withEnd(createdBooking.getReservation().getEndDateTime().toDateTime());

            BookingType booking = new BookingType()
                    .withID(String.valueOf(createdBooking.getIxsiBookingId())).withTimePeriod(timePeriod);
            bookingResponse.setBooking(booking);
            return bookingResponse;

        } catch (DatabaseException e) {
            return buildError(ErrorFactory.backendFailed(e.getMessage(), "Booking not possible due to backend error"));

        } catch (IxsiProcessingException e) {
            return buildError(ErrorFactory.bookingTargetNotAvail(e.getMessage(), e.getMessage()));
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
