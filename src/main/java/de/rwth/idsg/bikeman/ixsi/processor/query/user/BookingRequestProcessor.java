package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.TokenValidator;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.BookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.TimePeriodType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import de.rwth.idsg.bikeman.ixsi.service.BookingService;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.Duration;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingRequestProcessor implements
        UserRequestProcessor<BookingRequestType, BookingResponseType> {

    @Autowired BookingRepository bookingRepository;
    @Autowired BookingService bookingService;
    @Autowired TokenValidator tokenValidator;

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
        TokenValidator.Results results = tokenValidator.validate(userInfoList);

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
        // TODO
        Duration reservationDuration = request.getTimePeriodProposal().getMaxWait();

        // perform reservation and return id
        BookingType booking = new BookingType();
        try {
            long bookingId = bookingService.createBookingForUser(request.getBookingTargetID().getBookeeID(), user.getUserID(), reservationDuration);
            booking.setID(String.valueOf(bookingId));
            bookingResponse.setBooking(booking);
            return bookingResponse;
        } catch (DatabaseException e) {
            return buildError(ErrorFactory.backendFailed("Booking not possible", "Booking not possible"));
        }
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingResponseType buildError(ErrorType e) {
        BookingResponseType res = new BookingResponseType();
        res.getError().add(e);
        return res;
    }
}
