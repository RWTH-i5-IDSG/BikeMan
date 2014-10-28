package de.rwth.idsg.bikeman.ixsi.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.10.2014
 */
@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Inject private PasswordEncoder passwordEncoder;
    @PersistenceContext private EntityManager em;

    @Override
    public boolean validateUserToken(String userId, String token) {
        return false;
    }

    @Override
    public boolean setUserToken(String userId, String token) {
        return false;
    }
}
