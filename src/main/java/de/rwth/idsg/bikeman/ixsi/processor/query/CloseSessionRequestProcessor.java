package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.CloseSessionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CloseSessionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class CloseSessionRequestProcessor implements
        UserRequestProcessor<CloseSessionRequestType, CloseSessionResponseType> {

    @Override
    public UserResponseParams<CloseSessionResponseType> processAnonymously(CloseSessionRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.requestNotSupported());
    }

    @Override
    public UserResponseParams<CloseSessionResponseType> processForUser(CloseSessionRequestType request, Optional<Language> lan, UserInfoType userInfo) {
        return buildError(ErrorFactory.requestNotSupported());
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public UserResponseParams<CloseSessionResponseType> invalidSystem() {
        return buildError(ErrorFactory.requestNotSupported());
    }

    @Override
    public UserResponseParams<CloseSessionResponseType> invalidUserAuth() {
        return buildError(ErrorFactory.requestNotSupported());
    }

    private UserResponseParams<CloseSessionResponseType> buildError(ErrorType e) {
        CloseSessionResponseType res = new CloseSessionResponseType();
        res.getError().add(e);

        UserResponseParams<CloseSessionResponseType> u = new UserResponseParams<>();
        u.setResponse(res);
        return u;
    }

}