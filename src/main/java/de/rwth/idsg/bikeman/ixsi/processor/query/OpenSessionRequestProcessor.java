package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.OpenSessionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.OpenSessionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class OpenSessionRequestProcessor implements UserRequestProcessor<OpenSessionRequestType, OpenSessionResponseType> {

    @Override
    public UserResponseParams<OpenSessionResponseType> processAnonymously(OpenSessionRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.requestNotSupported());
    }

    @Override
    public UserResponseParams<OpenSessionResponseType> processForUser(OpenSessionRequestType request, Optional<Language> lan, UserInfoType userInfo) {
        return buildError(ErrorFactory.requestNotSupported());
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public UserResponseParams<OpenSessionResponseType> invalidSystem() {
        return buildError(ErrorFactory.requestNotSupported());
    }

    @Override
    public UserResponseParams<OpenSessionResponseType> invalidUserAuth() {
        return buildError(ErrorFactory.requestNotSupported());
    }

    private UserResponseParams<OpenSessionResponseType> buildError(ErrorType e) {
        OpenSessionResponseType res = new OpenSessionResponseType();
        res.getError().add(e);

        UserResponseParams<OpenSessionResponseType> u = new UserResponseParams<>();
        u.setResponse(res);
        return u;
    }
}