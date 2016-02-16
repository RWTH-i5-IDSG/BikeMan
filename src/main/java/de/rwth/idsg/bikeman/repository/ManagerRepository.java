package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Manager;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditManagerDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewManagerDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

import java.util.List;

/**
 * Spring Data JPA repository for the Manager entity.
 */
public interface ManagerRepository {

    List<ViewManagerDTO> findAll() throws DatabaseException;

    Manager create(CreateEditManagerDTO dto) throws DatabaseException;

    Manager update(CreateEditManagerDTO dto) throws DatabaseException;

    void delete(long userId) throws DatabaseException;
}
