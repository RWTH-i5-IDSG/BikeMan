package de.rwth.idsg.bikeman.ixsi.repository;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.10.2014
 */
public interface IxsiUserRepository {
    String setUserToken(String login, String password) throws DatabaseException;
    boolean validateUserToken(String login, String ixsiToken);
    boolean validateUserByMajorCustomer(String cardId, String majorCustomer);
    Optional<String> getMajorCustomerName(String cardId);
}
