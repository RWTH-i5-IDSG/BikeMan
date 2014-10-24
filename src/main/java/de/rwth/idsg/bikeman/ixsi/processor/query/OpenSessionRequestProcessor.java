package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.OpenSessionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.OpenSessionResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class OpenSessionRequestProcessor extends AbstractUserRequestProcessor<OpenSessionRequestType, OpenSessionResponseType> {

    @Override
    public UserResponseParams<OpenSessionResponseType> processAnonymously(Optional<Language> lan, OpenSessionRequestType request) {
        return respondWithError();
    }

    @Override
    public UserResponseParams<OpenSessionResponseType> processForUser(Optional<Language> lan, AuthType auth, OpenSessionRequestType request) {
        return respondWithError();
    }

    private UserResponseParams<OpenSessionResponseType> respondWithError() {
        ErrorType e = ErrorFactory.requestNotSupported();

        OpenSessionResponseType o = new OpenSessionResponseType();
        o.getError().add(e);

        UserResponseParams<OpenSessionResponseType> u = new UserResponseParams<>();
        u.setResponse(o);
        return u;
    }

}