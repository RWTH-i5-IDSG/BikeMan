package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
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
public class TokenGenerationRequestProcessor implements
        UserRequestProcessor<TokenGenerationRequestType, TokenGenerationResponseType> {

    @Autowired private IxsiUserRepository ixsiUserRepository;

    @Override
    public TokenGenerationResponseType processAnonymously(TokenGenerationRequestType request, Optional<Language> lan) {
        return buildError(ErrorFactory.notAllowedAnonym("Anonymous token generation makes no sense", null));
    }

    @Override
    public TokenGenerationResponseType processForUser(TokenGenerationRequestType request, Optional<Language> lan,
                                                      UserInfoType userInfo) {
        try {
            String token = ixsiUserRepository.setUserToken(userInfo.getUserID(), userInfo.getPassword());
            return new TokenGenerationResponseType().withToken(token);

        } catch (DatabaseException e) {
            log.error("Error occurred", e);
            return buildError(ErrorFactory.backendFailed(e.getLocalizedMessage(), null));
        }
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public TokenGenerationResponseType buildError(ErrorType e) {
        return new TokenGenerationResponseType().withError(e);
    }
}
