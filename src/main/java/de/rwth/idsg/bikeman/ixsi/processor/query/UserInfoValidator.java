package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.repository.IxsiUserRepository;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by max on 22/10/14.
 */
@Slf4j
@Component
public class UserInfoValidator {

    @Autowired private IxsiUserRepository ixsiUserRepository;

    public boolean validate(UserInfoType userInfo) {
        if (userInfo.isSetPassword()) {
            throw new IxsiProcessingException("We don't support using passwords");

        } else if (!IXSIConstants.Provider.id.equals(userInfo.getProviderID())) {
            throw new IxsiProcessingException("Not a Velocity user");
        }

        return ixsiUserRepository.validateUserToken(userInfo.getUserID(), userInfo.getToken());
    }

}