package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.PriceInformationRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PriceInformationResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

import java.util.List;

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
        return null;
    }

    /**
     * This method has to validate the user infos !!!!
     */
    @Override
    public PriceInformationResponseType processForUser(PriceInformationRequestType request, Optional<Language> lan,
                                                       List<UserInfoType> userInfoList) {
        // TODO FUTURE
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public PriceInformationResponseType invalidSystem() {
        return buildError(ErrorFactory.invalidSystem());
    }

    private PriceInformationResponseType buildError(ErrorType e) {
        PriceInformationResponseType res = new PriceInformationResponseType();
        res.getError().add(e);
        return res;
    }
}
