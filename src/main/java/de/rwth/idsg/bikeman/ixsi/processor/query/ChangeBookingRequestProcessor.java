package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeBookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeBookingResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserTriggeredRequestChoice;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class ChangeBookingRequestProcessor extends AbstractUserRequestProcessor<ChangeBookingRequestType, ChangeBookingResponseType> {

    @Override
    public UserResponseParams<ChangeBookingResponseType> processAnonymously(Optional<Language> lan, ChangeBookingRequestType request) {
        return super.processAnonymously(lan, request);
    }

    @Override
    public UserResponseParams<ChangeBookingResponseType> processForUser(Optional<Language> lan, AuthType auth, ChangeBookingRequestType request) {
        return super.processForUser(lan, auth, request);
    }
}