package de.rwth.idsg.bikeman.ixsi.repository;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.10.2014
 */
public interface UserRepository {

    boolean validateUserToken(String userId, String token);
    boolean setUserToken(String userId, String token);
}
