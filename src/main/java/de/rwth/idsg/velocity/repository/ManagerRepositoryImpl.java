package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Manager;
import de.rwth.idsg.velocity.domain.login.Authority;
import de.rwth.idsg.velocity.security.AuthoritiesConstants;
import de.rwth.idsg.velocity.web.rest.BackendException;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditManagerDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewManagerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

/**
 * Created by swam on 11/06/14.
 */

@Repository
@Transactional
@Slf4j
public class ManagerRepositoryImpl implements ManagerRepository {

    private enum Operation { CREATE, UPDATE };

    @PersistenceContext
    EntityManager em;

    @Inject
    PasswordEncoder passwordEncoder;

    @Override
    public List<ViewManagerDTO> findAll() throws BackendException {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();

            CriteriaQuery<ViewManagerDTO> criteria = builder.createQuery(ViewManagerDTO.class);
            Root<Manager> root = criteria.from(Manager.class);

            criteria.select(
                    builder.construct(
                            ViewManagerDTO.class,
                            root.get("userId"),
                            root.get("login")
                    )
            );

            return em.createQuery(criteria).getResultList();

        } catch (Exception e) {
            throw new BackendException("Failed during database operation.", e);
        }
    }

    @Override
    public void create(CreateEditManagerDTO dto) throws BackendException {
        Manager manager = new Manager();
        setFields(manager, dto, Operation.CREATE);

        try {
            em.persist(manager);
            log.debug("Created new manager {}", manager);

        } catch (EntityExistsException e) {
            throw new BackendException("This manager exists already.", e);

        } catch (Exception e) {
            throw new BackendException("Failed to create a new manager.", e);
        }
    }

    @Override
    public void update(CreateEditManagerDTO dto) throws BackendException {
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
            throw new BackendException("Failed to update manager with userId " + userId, e);
        }
    }

    @Override
    public void delete(long userId) throws BackendException {
        Manager manager = getManagerEntity(userId);
        try {
            em.remove(manager);
            log.debug("Deleted manager {}", manager);

        } catch (Exception e) {
            throw new BackendException("Failed to delete manager with userId " + userId, e);
        }
    }

    /**
     * Returns a manager, or throws exception when no manager exists.
     *
     */
    private Manager getManagerEntity(long userId) throws BackendException {
        Manager manager = em.find(Manager.class, userId);
        if (manager == null) {
            throw new BackendException("No manager with userId " + userId);
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
