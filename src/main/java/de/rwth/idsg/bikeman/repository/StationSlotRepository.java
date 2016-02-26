package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewErrorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Wolfgang Kluth on 18/06/14.
 */
public interface StationSlotRepository extends JpaRepository<StationSlot, Long> {

    StationSlot findByStationSlotPositionAndStationStationId(Integer stationSlotPosition, Long stationStationId);

    @Query("select stsl from StationSlot stsl where stsl.manufacturerId = ?1 AND stsl.station.manufacturerId = ?2")
    StationSlot findByManufacturerId(String slotManufacturerId, String stationManufacturerId);

    @Query("SELECT new de.rwth.idsg.bikeman.web.rest.dto.view.ViewErrorDTO(" +
            "sl.station.stationId, sl.stationSlotId, sl.station.manufacturerId, sl.manufacturerId," +
            " sl.station.name, sl.stationSlotPosition, sl.errorCode, sl.errorInfo, sl.station.updated)" +
            " FROM StationSlot sl  where not (sl.errorCode = '') and sl.errorCode is not null")
    List<ViewErrorDTO> findErrors();
}
