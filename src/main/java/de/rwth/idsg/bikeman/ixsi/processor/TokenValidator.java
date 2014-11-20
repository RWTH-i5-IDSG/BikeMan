package de.rwth.idsg.bikeman.ixsi.processor;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.repository.IxsiUserRepository;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 19.11.2014
 */
@Slf4j
@Component
public class TokenValidator {

    @Autowired private IxsiUserRepository ixsiUserRepository;

    public Results validate(List<UserInfoType> userInfoList) {
        Results r = new Results();
        for (UserInfoType userInfo : userInfoList) {
            validate(userInfo, r.getValidUsers(), r.getErrors());
        }
        return r;
    }

    /**
     * If a check fails, we don't really want to continue and check other fields but exit early.
     * Hence the "return"s.
     *
     */
    private void validate(UserInfoType userInfo, List<UserInfoType> validList, List<ErrorType> errorList) {

        if (!IXSIConstants.Provider.id.equals(userInfo.getProviderID())) {
            final String msg = "Not a " + IXSIConstants.Provider.id + " user";
            ErrorType e = ErrorFactory.invalidProvider(msg, msg);
            errorList.add(e);
            return;
        }

        if (userInfo.isSetPassword()) {
            ErrorType e = ErrorFactory.invalidRequest("Using plain passwords is not supported", null);
            errorList.add(e);
            return;
        }

        String userId = userInfo.getUserID();
        boolean isValid = ixsiUserRepository.validateUserToken(userId, userInfo.getToken());

        if (isValid) {
            validList.add(userInfo);

        } else {
            final String msg = "Token for user '" + userId + "' is invalid";
            ErrorType e = ErrorFactory.invalidUserToken(msg, msg);
            errorList.add(e);
        }
    }

    @Getter
    @Setter
    public class Results {
        private final List<UserInfoType> validUsers = new ArrayList<>();
        private final List<ErrorType> errors = new ArrayList<>();
    }

}