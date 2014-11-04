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

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Slf4j
@Component
public class TokenGenerationRequestProcessor implements UserRequestProcessor<TokenGenerationRequestType, TokenGenerationResponseType> {

    @Autowired private IxsiUserRepository ixsiUserRepository;

    @Override
    public UserResponseParams<TokenGenerationResponseType> processAnonymously(TokenGenerationRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.requestNotSupported());
    }

    @Override
    public UserResponseParams<TokenGenerationResponseType> processForUser(TokenGenerationRequestType request, Optional<Language> lan, UserInfoType userInfo) {
        try {
            String token = ixsiUserRepository.setUserToken(userInfo.getUserID(), userInfo.getPassword());
            TokenGenerationResponseType r = new TokenGenerationResponseType();
            r.setToken(token);

            UserResponseParams<TokenGenerationResponseType> u = new UserResponseParams<>();
            u.setResponse(r);
            return u;

        } catch (DatabaseException e) {
            log.error("Error occurred", e);
            return buildError(ErrorFactory.invalidUserAuth());
        }
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public UserResponseParams<TokenGenerationResponseType> invalidSystem() {
        return buildError(ErrorFactory.invalidSystem());
    }

    @Override
    public UserResponseParams<TokenGenerationResponseType> invalidUserAuth() {
        return buildError(ErrorFactory.invalidUserAuth());
    }

    private UserResponseParams<TokenGenerationResponseType> buildError(ErrorType e) {
        TokenGenerationResponseType res = new TokenGenerationResponseType();
        res.getError().add(e);

        UserResponseParams<TokenGenerationResponseType> u = new UserResponseParams<>();
        u.setResponse(res);
        return u;
    }
}