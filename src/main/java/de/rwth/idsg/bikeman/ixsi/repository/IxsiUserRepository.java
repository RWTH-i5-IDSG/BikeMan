package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.10.2014
 */
public interface IxsiUserRepository {
    String setUserToken(String login, String password) throws DatabaseException;
    boolean validateUserToken(String login, String ixsiToken);
}