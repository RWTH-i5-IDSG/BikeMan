package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class PlaceAvailabilityRequestProcessor implements UserRequestProcessor<PlaceAvailabilityRequestType, PlaceAvailabilityResponseType> {

    @Override
    public UserResponseParams<PlaceAvailabilityResponseType> process(Optional<Language> lan, AuthType auth, PlaceAvailabilityRequestType request) {
        return null;
    }
}