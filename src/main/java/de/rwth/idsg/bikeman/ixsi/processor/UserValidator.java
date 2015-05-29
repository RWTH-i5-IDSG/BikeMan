package de.rwth.idsg.bikeman.ixsi.processor;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.repository.IxsiUserRepository;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 19.11.2014
 */
@Slf4j
@Component
public class UserValidator {

    @Autowired private IxsiUserRepository ixsiUserRepository;

    private static final String MAJOR_CUSTOMER_NAME = "ASEAG"; // TODO: get mc-name from anywhere

    /**
     * If a check fails, we don't really want to continue and check other fields but exit early.
     * Hence the "return"s.
     *
     * Returns Optional.absent(), when the user is successfully validated, otherwise the according error type.
     */
    public Optional<ErrorType> validate(UserInfoType userInfo) {

        if (!IXSIConstants.Provider.id.equals(userInfo.getProviderID())) {
            final String msg = "Not a " + IXSIConstants.Provider.id + " user";
            return Optional.of(ErrorFactory.invalidProvider(msg, msg));
        }

        if (userInfo.isSetPassword() || userInfo.isSetToken()) {
            return Optional.of(ErrorFactory.invalidRequest("Using passwords or tokens is not supported", null));
        }

        String userId = userInfo.getUserID();
        Optional<String> opt = ixsiUserRepository.getMajorCustomerName(userId);

        if (opt.isPresent() && opt.get().equalsIgnoreCase(MAJOR_CUSTOMER_NAME)) {
            // Everything OK
            return Optional.absent();

        } else {
            final String msg = "User id '" + userId + "' is invalid";
            return Optional.of(ErrorFactory.invalidAuth(msg, msg));
        }
    }
}
