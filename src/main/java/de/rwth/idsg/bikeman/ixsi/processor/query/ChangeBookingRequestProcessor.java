package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilityResponseType;
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
    public UserResponseParams<ChangeBookingResponseType> processAnonymously(ChangeBookingRequestType request,
                                                                            Optional<Language> lan) {
        // TODO FUTURE
        return null;
    }

    @Override
    public UserResponseParams<ChangeBookingResponseType> processForUser(ChangeBookingRequestType request,
                                                                        Optional<Language> lan, UserInfoType userInfo) {
        // TODO FUTURE
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public UserResponseParams<ChangeBookingResponseType> invalidSystem() {
        return buildError(ErrorFactory.invalidSystem());
    }

    @Override
    public UserResponseParams<ChangeBookingResponseType> invalidUserAuth() {
        return buildError(ErrorFactory.invalidUserAuth());
    }

    private UserResponseParams<ChangeBookingResponseType> buildError(ErrorType e) {
        ChangeBookingResponseType res = new ChangeBookingResponseType();
        res.getError().add(e);

        UserResponseParams<ChangeBookingResponseType> u = new UserResponseParams<>();
        u.setResponse(res);
        return u;
    }
}