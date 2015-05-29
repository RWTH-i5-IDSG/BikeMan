package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeUserRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeUserResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import de.rwth.idsg.bikeman.ixsi.schema.UserType;
import de.rwth.idsg.bikeman.ixsi.service.IxsiUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired private IxsiUserService ixsiUserService;

    @Override
    public ChangeUserResponseType processAnonymously(ChangeUserRequestType request, Optional<Language> lan) {
        if (!request.isSetUser()) {
            return buildError(ErrorFactory.invalidRequest("User list may not be empty.", null));
        }

        try {
            List<UserType> acceptedUsers = ixsiUserService.changeUsers(request.getUser());
            return new ChangeUserResponseType().withUser(acceptedUsers);

        } catch (Exception e) {
            return buildError(ErrorFactory.backendFailed(e.getMessage(), null));
        }
    }

    @Override
    public ChangeUserResponseType processForUser(ChangeUserRequestType request, Optional<Language> lan,
                                                 UserInfoType userInfo) {
        // TODO
        return buildError(ErrorFactory.notImplemented(null, null));
    }

    @Override
    public ChangeUserResponseType buildError(ErrorType e) {
        return new ChangeUserResponseType().withError(e);
    }
}
