package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.ActivationKey;
import de.rwth.idsg.bikeman.domain.ActivationKeyType;
import de.rwth.idsg.bikeman.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Slf4j
public class ActivationKeyRepositoryImpl implements ActivationKeyRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public String create(Customer customer, ActivationKeyType type) {
        ActivationKey activationKey = new ActivationKey();

        activationKey.setCustomer(customer);
        activationKey.setType(type);
        activationKey.setKey(RandomStringUtils.randomAlphanumeric(20));
        activationKey.setValidUntil(LocalDateTime.now().plusHours(24));

        try {
            em.persist(activationKey);
            log.debug("Created new activationKey for customer {}", customer);
        } catch (Exception e) {
            log.error("Failed to create new activationKey: ", e);
        }

        return activationKey.getKey();
    }

    @Override
    public ActivationKey save(ActivationKey activationKey) {
        em.persist(activationKey);
        return activationKey;
    }

    @Override
    public Optional<ActivationKey> findNotUsedAndNotExpired(String key, ActivationKeyType type) {
        final String q = "SELECT k FROM ActivationKey k " +
            "WHERE k.key = :key " +
            "AND k.type = :type " +
            "AND k.used = false " +
            "AND k.validUntil > :now";

        try {
            return Optional.of(em.createQuery(q, ActivationKey.class)
                .setParameter("key", key)
                .setParameter("type", type)
                .setParameter("now", new LocalDateTime())
                .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
