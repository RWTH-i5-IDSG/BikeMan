package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserTriggeredRequestChoice;
import de.rwth.idsg.bikeman.ixsi.schema.UserTriggeredResponseChoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 22.10.2014
 */
@Slf4j
public abstract class AbstractUserRequestProcessor<T1 extends UserTriggeredRequestChoice, T2 extends UserTriggeredResponseChoice>
        implements UserRequestProcessor<T1, T2> {

    @Autowired TokenValidator tokenValidator;

    @Override
    public UserResponseParams<T2> process(Optional<Language> lan, AuthType auth, T1 request) {
        log.trace("Processing the authentication information...");

        if (auth == null) {
            throw new IxsiProcessingException("Missing authentication");

        } else if (auth.isSetAnonymous() && auth.isAnonymous()) {
            return processAnonymously(lan, request);

        } else if (auth.isSetUserInfo() && tokenValidator.validate(auth.getUserInfo())) {
            return processForUser(lan, auth, request);

        } else if (auth.isSetSessionID()) {
            throw new IxsiProcessingException("Session-based authentication is not supported by this party");

        } else {
            throw new IxsiProcessingException("Authentication requirements are not met");
        }
    }

    @Override
    public UserResponseParams<T2> processAnonymously(Optional<Language> lan, T1 request) {
        // Override in extending classes
        return null;
    }

    @Override
    public UserResponseParams<T2> processForUser(Optional<Language> lan, AuthType auth, T1 request) {
        // Override in extending classes
        return null;
    }
}