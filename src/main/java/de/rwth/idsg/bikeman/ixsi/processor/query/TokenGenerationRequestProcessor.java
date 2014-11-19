package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.repository.IxsiUserRepository;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.TokenGenerationRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.TokenGenerationResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Slf4j
@Component
public class TokenGenerationRequestProcessor implements
        UserRequestProcessor<TokenGenerationRequestType, TokenGenerationResponseType> {

    @Autowired private IxsiUserRepository ixsiUserRepository;
    @Autowired private UserInfoValidator userInfoValidator;

    @Override
    public TokenGenerationResponseType processAnonymously(TokenGenerationRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.requestNotSupported());
    }

    /**
     * This method has to validate the user infos !!!!
     */
    @Override
    public TokenGenerationResponseType processForUser(TokenGenerationRequestType request, Optional<Language> lan,
                                                      List<UserInfoType> userInfoList) {

        // For the token generation request there can be only one userInfo
        if (userInfoList.size() != 1) {
            return buildError(ErrorFactory.invalidRequest());
        }

        UserInfoType userInfo = userInfoList.get(0);
        if (!userInfoValidator.validate(userInfo)) {
            return buildError(ErrorFactory.invalidUserAuth());
        }

        try {
            String token = ixsiUserRepository.setUserToken(userInfo.getUserID(), userInfo.getPassword());
            TokenGenerationResponseType r = new TokenGenerationResponseType();
            r.setToken(token);
            return r;

        } catch (DatabaseException e) {
            log.error("Error occurred", e);
            return buildError(ErrorFactory.backendFailed());
        }
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public TokenGenerationResponseType invalidSystem() {
        return buildError(ErrorFactory.invalidSystem());
    }

    private TokenGenerationResponseType buildError(ErrorType e) {
        TokenGenerationResponseType res = new TokenGenerationResponseType();
        res.getError().add(e);
        return res;
    }
}