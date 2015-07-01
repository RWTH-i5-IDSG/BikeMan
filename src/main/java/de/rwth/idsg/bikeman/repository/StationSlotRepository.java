package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.StationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Wolfgang Kluth on 18/06/14.
 */
public interface StationSlotRepository extends JpaRepository<StationSlot, Long> {

    public StationSlot findByStationSlotPositionAndStationStationId(Integer stationSlotPosition, Long stationStationId);

    @Query("select stsl from StationSlot stsl where stsl.manufacturerId = ?1")
    public StationSlot findByManufacturerId(String manufacturerId);

}
