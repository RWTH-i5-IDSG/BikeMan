package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Manager;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditCustomerDTO;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateManagerDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewManagerDTO;

import java.util.List;

/**
 * Created by swam on 11/06/14.
 */
public interface ManagerRepository {

    /**
     * self-explanatory
     */
    List<ViewManagerDTO> findAll();

    /**
     * self-explanatory
     */
    void create(CreateManagerDTO dto);

    /**
     * self-explanatory
     */
    void update(Manager entity);

    /**
     * self-explanatory
     */
    void delete(String login);
}
