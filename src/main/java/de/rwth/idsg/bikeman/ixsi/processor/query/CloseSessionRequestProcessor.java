package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.CloseSessionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CloseSessionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class CloseSessionRequestProcessor implements UserRequestProcessor<CloseSessionRequestType, CloseSessionResponseType> {

    @Override
    public UserResponseParams<CloseSessionResponseType> process(Optional<Language> lan, AuthType auth, CloseSessionRequestType request) {
        return null;
    }
}