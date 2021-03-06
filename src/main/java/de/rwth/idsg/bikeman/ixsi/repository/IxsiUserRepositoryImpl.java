package de.rwth.idsg.bikeman.ixsi.repository;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.ixsi.IxsiToken;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.internal.QueryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Optional<String> getMajorCustomerName(String cardId) {
        final String p = "SELECT mj.name FROM MajorCustomer mj " +
                         "WHERE (SELECT ca FROM CardAccount ca WHERE ca.cardId = :cardId) " +
                         "MEMBER OF mj.cardAccounts";

        try {
            List<String> nameResult = em.createQuery(p, String.class)
                                        .setParameter("cardId", cardId)
                                        .getResultList();

            return Optional.fromNullable(getSingleFromList(nameResult));

        } catch (Exception e) {
            log.error("Error occurred", e);
            return Optional.absent();
        }
    }

    @Override
    public Optional<String> getCustomerId(String cardId) {
        final String p = "SELECT c.customerId FROM Customer c WHERE c.cardAccount.cardId = :cardId";

        try {
            List<String> nameResult = em.createQuery(p, String.class)
                                        .setParameter("cardId", cardId)
                                        .getResultList();

            return Optional.fromNullable(getSingleFromList(nameResult));

        } catch (Exception e) {
            log.error("Error occurred", e);
            return Optional.absent();
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

    /**
     * Captures the logic and essence of {@link QueryImpl#getSingleResult()} minus the exception throwing.
     */
    @Nullable
    private static <T> T getSingleFromList(List<T> result) {
        if (result.size() == 0) {
            return null;

        } else if (result.size() > 1) {
            Set<T> uniqueResult = new HashSet<>(result);
            if (uniqueResult.size() > 1) {
                log.warn("Query result returns more than one elements");
            }
            return uniqueResult.iterator().next();

        } else {
            return result.get(0);
        }
    }
}
