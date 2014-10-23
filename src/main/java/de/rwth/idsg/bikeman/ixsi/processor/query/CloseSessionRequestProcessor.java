package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.CloseSessionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CloseSessionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.OpenSessionResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class CloseSessionRequestProcessor extends AbstractUserRequestProcessor<CloseSessionRequestType, CloseSessionResponseType> {

    @Override
    public UserResponseParams<CloseSessionResponseType> processAnonymously(Optional<Language> lan, CloseSessionRequestType request) {
        return respondWithError();
    }

    @Override
    public UserResponseParams<CloseSessionResponseType> processForUser(Optional<Language> lan, AuthType auth, CloseSessionRequestType request) {
        return respondWithError();
    }

    private UserResponseParams<CloseSessionResponseType> respondWithError() {
        ErrorType e = ErrorFactory.requestNotSupported();

        CloseSessionResponseType o = new CloseSessionResponseType();
        o.getError().add(e);

        UserResponseParams<CloseSessionResponseType> u = new UserResponseParams<>();
        u.setResponse(o);
        return u;
    }
}