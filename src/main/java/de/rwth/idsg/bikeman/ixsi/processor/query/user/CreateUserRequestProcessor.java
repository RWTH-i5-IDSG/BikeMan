package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.service.IxsiUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.CreateUserRequestType;
import xjc.schema.ixsi.CreateUserResponseType;
import xjc.schema.ixsi.ErrorType;
import xjc.schema.ixsi.Language;
import xjc.schema.ixsi.UserInfoType;
import xjc.schema.ixsi.UserType;

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
    public Class<CreateUserRequestType> getProcessingClass() {
        return CreateUserRequestType.class;
    }

    @Override
    public CreateUserResponseType processAnonymously(CreateUserRequestType request, Optional<Language> lan) {
        if (!request.isSetUser()) {
            return buildError(ErrorFactory.Sys.invalidRequest("User list may not be empty.", null));
        }

        try {
            List<UserType> acceptedUsers = ixsiUserService.createUsers(request.getUser());
            return new CreateUserResponseType().withUser(acceptedUsers);

        } catch (Exception e) {
            return buildError(ErrorFactory.Sys.backendFailed(e.getMessage(), null));
        }
    }

    @Override
    public CreateUserResponseType processForUser(CreateUserRequestType request, Optional<Language> lan,
                                                 UserInfoType userInfo) {
        // TODO
        return buildError(ErrorFactory.Sys.notImplemented(null, null));
    }

    @Override
    public CreateUserResponseType buildError(ErrorType e) {
        return new CreateUserResponseType().withError(e);
    }
}
