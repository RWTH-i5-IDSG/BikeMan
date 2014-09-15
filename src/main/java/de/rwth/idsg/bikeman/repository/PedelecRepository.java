package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewPedelecDTO;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedelec entity.
 */
public interface PedelecRepository {

    List<ViewPedelecDTO> findAll() throws DatabaseException;
    ViewPedelecDTO findOneDTO(Long pedelecId) throws DatabaseException;
    Pedelec findOne(long pedelecId) throws DatabaseException;
    void create(CreateEditPedelecDTO dto) throws DatabaseException;
    void update(CreateEditPedelecDTO dto) throws DatabaseException;
    void delete(long pedelecId) throws DatabaseException;
}
