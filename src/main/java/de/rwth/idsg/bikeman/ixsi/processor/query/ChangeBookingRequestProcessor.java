package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeBookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeBookingResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class ChangeBookingRequestProcessor implements
        UserRequestProcessor<ChangeBookingRequestType, ChangeBookingResponseType> {

    @Override
    public ChangeBookingResponseType processAnonymously(ChangeBookingRequestType request, Optional<Language> lan) {
        // TODO FUTURE
        return null;
    }

    /**
     * This method has to validate the user infos !!!!
     */
    @Override
    public ChangeBookingResponseType processForUser(ChangeBookingRequestType request, Optional<Language> lan, List<UserInfoType> userInfoList) {
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public ChangeBookingResponseType invalidSystem() {
        return buildError(ErrorFactory.invalidSystem());
    }

    private ChangeBookingResponseType buildError(ErrorType e) {
        ChangeBookingResponseType res = new ChangeBookingResponseType();
        res.getError().add(e);
        return res;
    }
}
