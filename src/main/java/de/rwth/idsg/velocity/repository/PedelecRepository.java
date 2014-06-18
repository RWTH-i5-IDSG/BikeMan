package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.web.rest.BackendException;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewPedelecDTO;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedelec entity.
 */
public interface PedelecRepository {

    List<ViewPedelecDTO> findAll();
    ViewPedelecDTO findOneDTO(Long pedelecId) throws BackendException;
    Pedelec findOne(long pedelecId) throws BackendException;
    void create(CreateEditPedelecDTO dto) throws BackendException;
    void update(CreateEditPedelecDTO dto) throws BackendException;
    void delete(long pedelecId) throws BackendException;
}
