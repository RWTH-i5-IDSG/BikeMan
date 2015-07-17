package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.ErrorType;
import xjc.schema.ixsi.Language;
import xjc.schema.ixsi.OpenSessionRequestType;
import xjc.schema.ixsi.OpenSessionResponseType;
import xjc.schema.ixsi.UserInfoType;

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
