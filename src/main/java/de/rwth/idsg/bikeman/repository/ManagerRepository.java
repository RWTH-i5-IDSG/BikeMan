package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditManagerDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewManagerDTO;

import java.util.List;

/**
 * Spring Data JPA repository for the Manager entity.
 */
public interface ManagerRepository {

    List<ViewManagerDTO> findAll() throws DatabaseException;

    void create(CreateEditManagerDTO dto) throws DatabaseException;

    void update(CreateEditManagerDTO dto) throws DatabaseException;

    void delete(long userId) throws DatabaseException;
}
