package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.PriceInformationRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PriceInformationResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class PriceInformationRequestProcessor implements
        UserRequestProcessor<PriceInformationRequestType, PriceInformationResponseType> {

    @Override
    public PriceInformationResponseType processAnonymously(PriceInformationRequestType request, Optional<Language> lan) {
        // TODO FUTURE
        return buildError(ErrorFactory.notImplemented(null, null));
    }

    @Override
    public PriceInformationResponseType processForUser(PriceInformationRequestType request, Optional<Language> lan,
                                                       UserInfoType userInfo) {
        // TODO FUTURE
        return buildError(ErrorFactory.notImplemented(null, null));
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public PriceInformationResponseType buildError(ErrorType e) {
        return new PriceInformationResponseType().withError(e);
    }
}
