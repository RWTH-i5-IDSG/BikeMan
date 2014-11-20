package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.BookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingRequestProcessor implements
        UserRequestProcessor<BookingRequestType, BookingResponseType> {

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
        // TODO FUTURE
        return null;
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
