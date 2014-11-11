package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.domain.MajorCustomer;
import de.rwth.idsg.bikeman.domain.ixsi.IxsiToken;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Date;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.10.2014
 */
@Slf4j
@Repository
public class IxsiUserRepositoryImpl implements IxsiUserRepository {

    @Inject private PasswordEncoder passwordEncoder;
    @PersistenceContext private EntityManager em;

    @Override
    @Transactional(readOnly = false)
    public String setUserToken(String login, String rawPassword) throws DatabaseException {
        MajorCustomer majorCustomer = getMajorCustomer(login, rawPassword);

        final String q = "SELECT t FROM IxsiToken t WHERE t.majorCustomer = :majorCustomer";

        String encodedPassword = passwordEncoder.encode(rawPassword);

        // If there is already a token, update it.
        try {
            IxsiToken t = em.createQuery(q, IxsiToken.class)
                            .setParameter("majorCustomer", majorCustomer)
                            .getSingleResult();
            t.setTokenValue(encodedPassword);
            t.setCreated(new Date());
            t.setLastUsed(null);
            em.merge(t);
            log.debug("Updated IxsiToken for {}", majorCustomer);

        // Insert a new token for customer
        } catch (NoResultException e) {
            log.error("Error occurred", e);

            IxsiToken newToken = new IxsiToken();
            newToken.setMajorCustomer(majorCustomer);
            newToken.setTokenValue(encodedPassword);
            em.persist(newToken);
            log.debug("Inserted IxsiToken for {}", majorCustomer);
        }

        return encodedPassword;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean validateUserToken(String login, String ixsiToken) {
        final String q = "SELECT t FROM IxsiToken t " +
                         "WHERE t.tokenValue = :ixsiToken " +
                         "AND t.majorCustomer.login = :login";

        try {
            IxsiToken t = em.createQuery(q, IxsiToken.class)
                            .setParameter("login", login)
                            .setParameter("ixsiToken", ixsiToken)
                            .getSingleResult();

            log.info("IxsiToken: {}", t);

            t.setLastUsed(new Date());
            em.merge(t);
            return true;

        } catch (Exception e) {
            log.error("Error occurred", e);
            return false;
        }
    }

    /**
     * Does the MajorCustomer exist, at all? And is the password correct?
     *
     */
    private MajorCustomer getMajorCustomer(String login, String rawPassword) throws DatabaseException {
        final String p = "SELECT mc FROM MajorCustomer mc WHERE mc.login = :login";

        try {
            MajorCustomer majorCustomer = em.createQuery(p, MajorCustomer.class)
                                            .setParameter("login", login)
                                            .getSingleResult();

            if (!passwordEncoder.matches(rawPassword, majorCustomer.getPassword())) {
                throw new DatabaseException("MajorCustomer password is not correct");
            }

            return majorCustomer;

        } catch (NoResultException e) {
            throw new DatabaseException("No MajorCustomer exists with login " + login, e);
        }
    }

}
