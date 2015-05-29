package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeBookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeBookingResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class ChangeBookingRequestProcessor implements
        UserRequestProcessor<ChangeBookingRequestType, ChangeBookingResponseType> {

    @Override
    public ChangeBookingResponseType processAnonymously(ChangeBookingRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.notAllowedAnonym("Anonymous change booking request not allowed", null));
    }

    @Override
    public ChangeBookingResponseType processForUser(ChangeBookingRequestType request, Optional<Language> lan,
                                                    UserInfoType userInfo) {
        // TODO
        return buildError(ErrorFactory.notImplemented(null, null));
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public ChangeBookingResponseType buildError(ErrorType e) {
        return new ChangeBookingResponseType().withError(e);
    }
}
