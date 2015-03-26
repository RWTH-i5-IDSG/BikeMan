package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2014
 */
@Slf4j
@Component
public class BookingUnlockRequestProcessor implements
        UserRequestProcessor<BookingUnlockRequestType, BookingUnlockResponseType> {

    @Override
    public BookingUnlockResponseType processAnonymously(BookingUnlockRequestType request, Optional<Language> lan) {
        return null;
    }

    /**
     * This method has to validate the user infos !!!!
     */
    @Override
    public BookingUnlockResponseType processForUser(BookingUnlockRequestType request, Optional<Language> lan,
                                                    List<UserInfoType> userInfoList) {
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingUnlockResponseType buildError(ErrorType e) {
        return new BookingUnlockResponseType().withError(e);
    }
}
