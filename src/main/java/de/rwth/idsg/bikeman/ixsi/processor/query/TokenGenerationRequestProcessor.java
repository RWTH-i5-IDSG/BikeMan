package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilityResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.TokenGenerationRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.TokenGenerationResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class TokenGenerationRequestProcessor implements UserRequestProcessor<TokenGenerationRequestType, TokenGenerationResponseType> {

    @Override
    public UserResponseParams<TokenGenerationResponseType> processAnonymously(TokenGenerationRequestType request, Optional<Language> lan) {
        return null;
    }

    @Override
    public UserResponseParams<TokenGenerationResponseType> processForUser(TokenGenerationRequestType request, Optional<Language> lan, UserInfoType userInfo) {
        return null;
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