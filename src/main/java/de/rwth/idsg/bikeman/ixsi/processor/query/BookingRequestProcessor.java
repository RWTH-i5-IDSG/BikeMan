package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilityResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingRequestProcessor implements
        UserRequestProcessor<BookingRequestType, BookingResponseType> {

    @Override
    public UserResponseParams<BookingResponseType> processAnonymously(BookingRequestType request,
                                                                      Optional<Language> lan) {
        return null;
    }

    @Override
    public UserResponseParams<BookingResponseType> processForUser(BookingRequestType request,
                                                                  Optional<Language> lan, UserInfoType userInfo) {
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public UserResponseParams<BookingResponseType> invalidSystem() {
        return buildError(ErrorFactory.invalidSystem());
    }

    @Override
    public UserResponseParams<BookingResponseType> invalidUserAuth() {
        return buildError(ErrorFactory.invalidUserAuth());
    }

    private UserResponseParams<BookingResponseType> buildError(ErrorType e) {
        BookingResponseType res = new BookingResponseType();
        res.getError().add(e);

        UserResponseParams<BookingResponseType> u = new UserResponseParams<>();
        u.setResponse(res);
        return u;
    }
}