package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.domain.ixsi.IxsiClientSystem;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 06.11.2014
 */
@Slf4j
@Repository
public class SystemValidatorImpl implements SystemValidator {

    @PersistenceContext private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public String getSystemID(String ipAddress) throws DatabaseException {
        final String q = "SELECT cs.systemId FROM IxsiClientSystem cs WHERE cs.ipAddress = :ipAddress";

        try {
            return em.createQuery(q, String.class)
                     .setParameter("ipAddress", ipAddress)
                     .getSingleResult();

        } catch (NoResultException e) {
            throw new DatabaseException("systemId for ipAddress '" + ipAddress + "' cannot be found", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validate(String systemId) {
        final String q = "SELECT 1 FROM IxsiClientSystem cs WHERE cs.systemId = :systemId";

        try {
            em.createQuery(q)
              .setParameter("systemId", systemId)
              .getSingleResult();
            return true;

        } catch (Exception e) {
            log.error("Error occurred", e);
            return false;
        }
    }

//    @Override
//    @Transactional(readOnly = false)
//    public void create(String systemId, String ipAddress) throws DatabaseException {
//        IxsiClientSystem clientSystem = new IxsiClientSystem();
//        clientSystem.setSystemId(systemId);
//        clientSystem.setIpAddress(ipAddress);
//
//        try {
//            em.persist(clientSystem);
//            log.debug("Created new IxsiClientSystem {}", clientSystem);
//
//        } catch (EntityExistsException e) {
//            throw new DatabaseException("This IxsiClientSystem exists already.", e);
//
//        } catch (Exception e) {
//            throw new DatabaseException("Failed to create a new IxsiClientSystem.", e);
//        }
//    }
}
