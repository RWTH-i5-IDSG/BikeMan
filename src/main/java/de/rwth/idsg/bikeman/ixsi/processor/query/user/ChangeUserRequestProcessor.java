package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.TokenValidator;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeUserRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeUserResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import de.rwth.idsg.bikeman.ixsi.schema.UserType;
import de.rwth.idsg.bikeman.ixsi.service.UserService;
import de.rwth.idsg.bikeman.service.CardAccountService;
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

    @Autowired
    UserService userService;
    @Autowired
    TokenValidator tokenValidator;


    @Override
    public ChangeUserResponseType processAnonymously(ChangeUserRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.invalidRequest("Anonymous requests not possible.", null));
    }

    /**
     * This method has to validate the user infos !!!!
     */
    @Override
    public ChangeUserResponseType processForUser(ChangeUserRequestType request, Optional<Language> lan,
                                                 List<UserInfoType> userInfoList) {
        ChangeUserResponseType response = new ChangeUserResponseType();

        //TODO is this really necessary in the given context?
        TokenValidator.Results results = tokenValidator.validate(userInfoList);
        List<ErrorType> errors = results.getErrors();
        if (!errors.isEmpty()) {
            response.getError().addAll(errors);
        }

        if (!request.isSetUser() || request.getUser().isEmpty()) {
            return buildError(ErrorFactory.invalidRequest("User list may not be empty.", null));
        }

        List<UserType> acceptedUsers = userService.changeUsers(request.getUser());
        response.getUser().addAll(acceptedUsers);

        return response;
    }

    @Override
    public ChangeUserResponseType buildError(ErrorType e) {
        return new ChangeUserResponseType().withError(e);
    }
}
