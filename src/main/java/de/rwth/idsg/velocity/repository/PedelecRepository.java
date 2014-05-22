package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewPedelecDTO;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedelec entity.
 */
public interface PedelecRepository {

    List<Pedelec> findAll();
    Pedelec findOne(Long id);
    void save(Pedelec pedelec);
    void delete(Long id);
    List<ViewPedelecDTO> viewPedelecs();

}
