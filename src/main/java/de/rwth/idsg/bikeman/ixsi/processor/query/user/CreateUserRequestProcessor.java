package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.TokenValidator;
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

    @Autowired
    IxsiUserService ixsiUserService;
    @Autowired
    TokenValidator tokenValidator;

    @Override
    public CreateUserResponseType processAnonymously(CreateUserRequestType request, Optional<Language> lan) {
        CreateUserResponseType response = new CreateUserResponseType();

        if (!request.isSetUser() || request.getUser().isEmpty()) {
            return buildError(ErrorFactory.invalidRequest("User list may not be empty.", null));
        }

        List<UserType> acceptedUsers = ixsiUserService.createUsers(request.getUser());
        response.getUser().addAll(acceptedUsers);

        return response;
    }

    /**
     * This method has to validate the user infos !!!!
     */
    @Override
    public CreateUserResponseType processForUser(CreateUserRequestType request, Optional<Language> lan,
                                                 List<UserInfoType> userInfoList) {
        CreateUserResponseType response = new CreateUserResponseType();

        //TODO is this really necessary in the given context?
        TokenValidator.Results results = tokenValidator.validate(userInfoList);
        List<ErrorType> errors = results.getErrors();
        if (!errors.isEmpty()) {
            response.getError().addAll(errors);
        }

        if (!request.isSetUser() || request.getUser().isEmpty()) {
            return buildError(ErrorFactory.invalidRequest("User list may not be empty.", null));
        }

        List<UserType> acceptedUsers = ixsiUserService.createUsers(request.getUser());
        response.getUser().addAll(acceptedUsers);

        return response;
    }

    @Override
    public CreateUserResponseType buildError(ErrorType e) {
        return new CreateUserResponseType().withError(e);
    }
}
