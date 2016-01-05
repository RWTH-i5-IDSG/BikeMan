package de.rwth.idsg.bikeman.repository;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewErrorDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedelec entity.
 */
public interface PedelecRepository {
    List<ViewPedelecDTO> findAll() throws DatabaseException;

    ViewPedelecDTO findOneDTO(Long pedelecId) throws DatabaseException;

    Pedelec findOne(long pedelecId) throws DatabaseException;

    Pedelec findByManufacturerId(String manufacturerId) throws DatabaseException;

    List<Pedelec> findByStation(String stationManufacturerId) throws DatabaseException;

    List<String> findManufacturerIdsByStation(String stationManufacturerId) throws DatabaseException;

    Optional<Pedelec> findPedelecsByStationSlot(String stationManufacturerId, String stationSlotManufacturerId) throws DatabaseException;

    List<ViewErrorDTO> findErrors() throws DatabaseException;

    Pedelec findByStationSlot(String stationSlotManufacturerId) throws DatabaseException;

    void create(CreateEditPedelecDTO dto) throws DatabaseException;

    void update(CreateEditPedelecDTO dto) throws DatabaseException;

    void delete(long pedelecId) throws DatabaseException;
}
