package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Manager;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateManagerDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewCustomerDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewManagerDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Created by swam on 11/06/14.
 */
public class ManagerRepositoryImpl implements ManagerRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ViewManagerDTO> findAll() {
        return null;
    }

    @Override
    public void create(CreateManagerDTO dto) {

    }

    @Override
    public void update(Manager entity) {

    }

    @Override
    public void delete(String login) {

    }
}
