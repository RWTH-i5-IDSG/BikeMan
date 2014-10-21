package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserTriggeredRequestChoice;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class TokenGenerationRequestProcessor implements UserRequestProcessor {

    @Override
    public UserResponseParams process(Language lan, AuthType auth, UserTriggeredRequestChoice c) {
        return null;
    }
}