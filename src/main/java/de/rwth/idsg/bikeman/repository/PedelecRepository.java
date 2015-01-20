package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.psinterface.dto.response.AvailablePedelecDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewPedelecDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedelec entity.
 */
public interface PedelecRepository {

    List<ViewPedelecDTO> findAll() throws DatabaseException;
    List<AvailablePedelecDTO> findAvailablePedelecs(Long stationId) throws DatabaseException;
    ViewPedelecDTO findOneDTO(Long pedelecId) throws DatabaseException;
    Pedelec findOne(long pedelecId) throws DatabaseException;
    public Pedelec findByManufacturerId(String manufacturerId) throws DatabaseException;
    void create(CreateEditPedelecDTO dto) throws DatabaseException;
    void update(CreateEditPedelecDTO dto) throws DatabaseException;
    void delete(long pedelecId) throws DatabaseException;
}
