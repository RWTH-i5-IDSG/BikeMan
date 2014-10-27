package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class PlaceAvailabilityRequestProcessor implements UserRequestProcessor<PlaceAvailabilityRequestType, PlaceAvailabilityResponseType> {

    @Override
    public UserResponseParams<PlaceAvailabilityResponseType> processAnonymously(PlaceAvailabilityRequestType request, Optional<Language> lan) {
        return null;
    }

    @Override
    public UserResponseParams<PlaceAvailabilityResponseType> processForUser(PlaceAvailabilityRequestType request, Optional<Language> lan, UserInfoType userInfo) {
        return null;
    }

    @Override
    public UserResponseParams<PlaceAvailabilityResponseType> invalidSystem() {
        return null;
    }

    @Override
    public UserResponseParams<PlaceAvailabilityResponseType> invalidUserAuth() {
        return null;
    }
}