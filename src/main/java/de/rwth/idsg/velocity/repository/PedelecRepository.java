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
    Pedelec findOne(Long pedelecId);
    void create(CreateEditPedelecDTO pedelec);
    void update(CreateEditPedelecDTO pedelec);
    void delete(Long pedelecId);
}
