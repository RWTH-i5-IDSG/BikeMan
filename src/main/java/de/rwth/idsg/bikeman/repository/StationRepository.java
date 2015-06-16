package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

import java.util.List;

/**
 * Spring Data JPA repository for the Station entity.
 */
public interface StationRepository {

    List<ViewStationDTO> findAll() throws DatabaseException;
    ViewStationDTO findOne(long stationId) throws DatabaseException;

    String getEndpointAddress(long stationId) throws DatabaseException;

    void create(CreateEditStationDTO dto) throws DatabaseException;
    void update(CreateEditStationDTO dto) throws DatabaseException;

    void changeSlotState(long stationId, int slotPosition, OperationState state);

}
