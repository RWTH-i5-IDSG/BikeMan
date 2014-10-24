package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.TokenGenerationRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.TokenGenerationResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class TokenGenerationRequestProcessor extends AbstractUserRequestProcessor<TokenGenerationRequestType, TokenGenerationResponseType> {

    @Override
    public UserResponseParams<TokenGenerationResponseType> processAnonymously(Optional<Language> lan, TokenGenerationRequestType request) {
        return super.processAnonymously(lan, request);
    }

    @Override
    public UserResponseParams<TokenGenerationResponseType> processForUser(Optional<Language> lan, AuthType auth, TokenGenerationRequestType request) {
        return super.processForUser(lan, auth, request);
    }
}