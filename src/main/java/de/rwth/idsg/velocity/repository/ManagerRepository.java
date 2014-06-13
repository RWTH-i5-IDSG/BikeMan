package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.web.rest.BackendException;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditManagerDTO;
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
    void create(CreateEditManagerDTO dto) throws BackendException;

    /**
     * self-explanatory
     */
    void update(CreateEditManagerDTO dto) throws BackendException;

    /**
     * self-explanatory
     */
    void delete(long userId) throws BackendException;
}
