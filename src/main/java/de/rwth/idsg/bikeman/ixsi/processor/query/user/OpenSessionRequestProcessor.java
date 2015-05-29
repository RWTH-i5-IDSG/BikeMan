package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
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
public class OpenSessionRequestProcessor implements
        UserRequestProcessor<OpenSessionRequestType, OpenSessionResponseType> {

    @Override
    public OpenSessionResponseType processAnonymously(OpenSessionRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.Sys.notImplemented("We don't support sessions", null));
    }

    @Override
    public OpenSessionResponseType processForUser(OpenSessionRequestType request, Optional<Language> lan,
                                                  UserInfoType userInfo) {
        return buildError(ErrorFactory.Sys.notImplemented("We don't support sessions", null));
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public OpenSessionResponseType buildError(ErrorType e) {
        return new OpenSessionResponseType().withError(e);
    }
}
