package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewPedelecDTO;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedelec entity.
 */
public interface PedelecRepository {

    List<ViewPedelecDTO> findAll();
    ViewPedelecDTO findOneDTO(Long pedelecId);
    Pedelec findOne(long pedelecId);
    void create(CreateEditPedelecDTO dto);
    void update(CreateEditPedelecDTO dto);
    void delete(long pedelecId);
}
