package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.BookingUnlockResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeUserRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeUserResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.CreateUserRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CreateUserResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 06.05.2015
 */
@Slf4j
@Component
public class ChangeUserRequestProcessor implements
        UserRequestProcessor<ChangeUserRequestType, ChangeUserResponseType> {


    @Override
    public ChangeUserResponseType processAnonymously(ChangeUserRequestType request, Optional<Language> lan) {
        return null;
    }

    /**
     * This method has to validate the user infos !!!!
     */
    @Override
    public ChangeUserResponseType processForUser(ChangeUserRequestType request, Optional<Language> lan,
                                                 List<UserInfoType> userInfoList) {
        return null;
    }

    @Override
    public ChangeUserResponseType buildError(ErrorType e) {
        return new ChangeUserResponseType().withError(e);
    }
}
