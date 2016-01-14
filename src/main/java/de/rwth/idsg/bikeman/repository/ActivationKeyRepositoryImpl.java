package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.ActivationKey;
import de.rwth.idsg.bikeman.domain.ActivationKeyType;
import de.rwth.idsg.bikeman.domain.Customer;
import liquibase.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Slf4j
public class ActivationKeyRepositoryImpl implements ActivationKeyRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public String create(Customer customer, ActivationKeyType type) {
        ActivationKey activationKey = new ActivationKey();

        activationKey.setCreatedAt(LocalDateTime.now());
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
}
