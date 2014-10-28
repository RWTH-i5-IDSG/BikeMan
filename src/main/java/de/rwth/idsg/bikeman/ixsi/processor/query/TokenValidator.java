package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.repository.UserRepository;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by max on 22/10/14.
 */
@Slf4j
@Component
public class TokenValidator {

    @Autowired private UserRepository userRepository;

    public boolean validate(UserInfoType userInfo) {
        if (userInfo.isSetPassword()) {
            throw new IxsiProcessingException("We don't support using passwords");

        } else if (!IXSIConstants.Provider.id.equals(userInfo.getProviderID().getValue())) {
            throw new IxsiProcessingException("Not a Velocity user");
        }

        return userRepository.validateUserToken(userInfo.getUserID().getValue(), userInfo.getToken());
    }

}