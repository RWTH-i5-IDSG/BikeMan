package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.domain.CardAccount;
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
    public String setUserToken(String cardId, String cardPin) throws DatabaseException {
        CardAccount cardAccount = getCardAccount(cardId, cardPin);

        final String q = "SELECT t FROM IxsiToken t WHERE t.cardAccount = :cardAccount";

        String encodedPin = passwordEncoder.encode(cardPin);

        // If there is already a token, update it.
        try {
            IxsiToken t = em.createQuery(q, IxsiToken.class)
                            .setParameter("cardAccount", cardAccount)
                            .getSingleResult();

            t.setTokenValue(encodedPin);
            t.setCreated(new Date());
            t.setLastUsed(null);
            em.merge(t);
            log.debug("Updated IxsiToken for {}", cardAccount);

        // Insert a new token for cardAccount
        } catch (NoResultException e) {
            log.error("Error occurred", e);

            IxsiToken newToken = new IxsiToken();
            newToken.setCardAccount(cardAccount);
            newToken.setTokenValue(encodedPin);
            em.persist(newToken);
            log.debug("Inserted IxsiToken for {}", cardAccount);
        }

        return encodedPin;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean validateUserToken(String cardId, String ixsiToken) {
        final String q = "SELECT t FROM IxsiToken t " +
                         "WHERE t.tokenValue = :ixsiToken " +
                         "AND t.cardAccount.cardId = :cardId";

        try {
            IxsiToken t = em.createQuery(q, IxsiToken.class)
                            .setParameter("cardId", cardId)
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
     * Does the CardAccount exist, at all? And is the pin correct?
     */
    private CardAccount getCardAccount(String cardId, String cardPin) throws DatabaseException {
        final String p = "SELECT ca FROM CardAccount ca WHERE ca.cardId = :cardId";

        try {
            CardAccount cardAccount = em.createQuery(p, CardAccount.class)
                                        .setParameter("cardId", cardId)
                                        .getSingleResult();

            if (!cardAccount.getCardPin().equals(cardPin)) {
                throw new DatabaseException("CardAccount pin is not correct");
            }

            return cardAccount;

        } catch (NoResultException e) {
            throw new DatabaseException("No CardAccount exists with cardId " + cardId, e);
        }
    }

}
