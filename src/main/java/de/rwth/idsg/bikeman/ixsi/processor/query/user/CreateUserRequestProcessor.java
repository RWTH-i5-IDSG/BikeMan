package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.CreateUserRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CreateUserResponseType;
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
public class CreateUserRequestProcessor implements
        UserRequestProcessor<CreateUserRequestType, CreateUserResponseType> {

    @Autowired private IxsiUserService ixsiUserService;

    @Override
    public CreateUserResponseType processAnonymously(CreateUserRequestType request, Optional<Language> lan) {
        if (!request.isSetUser()) {
            return buildError(ErrorFactory.invalidRequest("User list may not be empty.", null));
        }

        try {
            List<UserType> acceptedUsers = ixsiUserService.createUsers(request.getUser());
            return new CreateUserResponseType().withUser(acceptedUsers);

        } catch (Exception e) {
            return buildError(ErrorFactory.backendFailed(e.getMessage(), null));
        }
    }

    @Override
    public CreateUserResponseType processForUser(CreateUserRequestType request, Optional<Language> lan,
                                                 UserInfoType userInfo) {
        // TODO
        return buildError(ErrorFactory.notImplemented(null, null));
    }

    @Override
    public CreateUserResponseType buildError(ErrorType e) {
        return new CreateUserResponseType().withError(e);
    }
}
