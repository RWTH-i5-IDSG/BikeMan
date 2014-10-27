package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.schema.BookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingResponseType;
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

    @Override
    public UserResponseParams<BookingResponseType> invalidSystem() {
        return null;
    }

    @Override
    public UserResponseParams<BookingResponseType> invalidUserAuth() {
        return null;
    }
}