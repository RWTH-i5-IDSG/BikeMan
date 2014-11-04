package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilityResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.PriceInformationRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PriceInformationResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class PriceInformationRequestProcessor implements UserRequestProcessor<PriceInformationRequestType, PriceInformationResponseType> {

    @Override
    public UserResponseParams<PriceInformationResponseType> processAnonymously(PriceInformationRequestType request, Optional<Language> lan) {
        // TODO FUTURE
        return null;
    }

    @Override
    public UserResponseParams<PriceInformationResponseType> processForUser(PriceInformationRequestType request, Optional<Language> lan, UserInfoType userInfo) {
        // TODO FUTURE
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public UserResponseParams<PriceInformationResponseType> invalidSystem() {
        return buildError(ErrorFactory.invalidSystem());
    }

    @Override
    public UserResponseParams<PriceInformationResponseType> invalidUserAuth() {
        return buildError(ErrorFactory.invalidUserAuth());
    }

    private UserResponseParams<PriceInformationResponseType> buildError(ErrorType e) {
        PriceInformationResponseType res = new PriceInformationResponseType();
        res.getError().add(e);

        UserResponseParams<PriceInformationResponseType> u = new UserResponseParams<>();
        u.setResponse(res);
        return u;
    }
}