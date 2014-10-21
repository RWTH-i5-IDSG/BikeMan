package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.OpenSessionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.OpenSessionResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class OpenSessionRequestProcessor implements UserRequestProcessor<OpenSessionRequestType, OpenSessionResponseType> {

    @Override
    public UserResponseParams<OpenSessionResponseType> process(Optional<Language> lan, AuthType auth, OpenSessionRequestType request) {
        return null;
    }
}