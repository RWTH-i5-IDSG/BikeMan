package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
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
        return null;
    }

    @Override
    public UserResponseParams<PriceInformationResponseType> processForUser(PriceInformationRequestType request, Optional<Language> lan, UserInfoType userInfo) {
        return null;
    }

    @Override
    public UserResponseParams<PriceInformationResponseType> invalidSystem() {
        return null;
    }

    @Override
    public UserResponseParams<PriceInformationResponseType> invalidUserAuth() {
        return null;
    }
}