package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Manager;
import de.rwth.idsg.bikeman.domain.login.Authority;
import de.rwth.idsg.bikeman.security.AuthoritiesConstants;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditManagerDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewManagerDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Wolfgang Kluth on 11/06/14.
 */
@Repository
@Slf4j
public class ManagerRepositoryImpl implements ManagerRepository {

    private enum Operation { CREATE, UPDATE };

    @PersistenceContext private EntityManager em;
    @Inject private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<ViewManagerDTO> findAll() throws DatabaseException {

        final String q = "SELECT new de.rwth.idsg.bikeman.web.rest.dto.view." +
                         "ViewManagerDTO(m.userId, m.login) " +
                         "FROM Manager m";
        try {
            return em.createQuery(q, ViewManagerDTO.class)
                     .getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateEditManagerDTO dto) throws DatabaseException {
        Manager manager = new Manager();
        setFields(manager, dto, Operation.CREATE);

        try {
            em.persist(manager);
            log.debug("Created new manager {}", manager);

        } catch (EntityExistsException e) {
            throw new DatabaseException("This manager exists already.", e);

        } catch (Exception e) {
            throw new DatabaseException("Failed to create a new manager.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CreateEditManagerDTO dto) throws DatabaseException {
        final Long userId = dto.getUserId();
        if (userId == null) {
            return;
        }

        Manager manager = getManagerEntity(userId);
        setFields(manager, dto, Operation.UPDATE);

        try {
            em.merge(manager);
            log.debug("Updated manager {}", manager);

        } catch (Exception e) {
            throw new DatabaseException("Failed to update manager with userId " + userId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long userId) throws DatabaseException {
        Manager manager = getManagerEntity(userId);
        try {
            em.remove(manager);
            log.debug("Deleted manager {}", manager);

        } catch (Exception e) {
            throw new DatabaseException("Failed to delete manager with userId " + userId, e);
        }
    }

    /**
     * Returns a manager, or throws exception when no manager exists.
     *
     */
    @Transactional(readOnly = true)
    private Manager getManagerEntity(long userId) throws DatabaseException {
        Manager manager = em.find(Manager.class, userId);
        if (manager == null) {
            throw new DatabaseException("No manager with userId " + userId);
        } else {
            return manager;
        }
    }

    /**
     * This method sets the fields of the manager to the values in DTO.
     *
     * Important: The ID is not set!
     *
     */
    private void setFields(Manager manager, CreateEditManagerDTO dto, Operation operation) {
        manager.setLogin(dto.getLogin());

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        manager.setPassword(encryptedPassword);

        switch (operation) {
            case CREATE:
                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(new Authority(AuthoritiesConstants.MANAGER));
                manager.setAuthorities(authorities);
                break;

            case UPDATE:
                break;
        }
    }
}
