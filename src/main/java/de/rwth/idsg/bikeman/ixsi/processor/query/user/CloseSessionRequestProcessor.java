package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.CloseSessionRequestType;
import xjc.schema.ixsi.CloseSessionResponseType;
import xjc.schema.ixsi.ErrorType;
import xjc.schema.ixsi.Language;
import xjc.schema.ixsi.UserInfoType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class CloseSessionRequestProcessor implements
        UserRequestProcessor<CloseSessionRequestType, CloseSessionResponseType> {

    @Override
    public CloseSessionResponseType processAnonymously(CloseSessionRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.Sys.notImplemented("We don't support sessions", null));
    }

    @Override
    public CloseSessionResponseType processForUser(CloseSessionRequestType request, Optional<Language> lan,
                                                   UserInfoType userInfo) {
        return buildError(ErrorFactory.Sys.notImplemented("We don't support sessions", null));
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public CloseSessionResponseType buildError(ErrorType e) {
        return new CloseSessionResponseType().withError(e);
    }

}
