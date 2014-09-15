package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.StationSlot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Wolfgang Kluth on 18/06/14.
 */
public interface StationSlotRepository extends JpaRepository<StationSlot, Long> {

    public StationSlot findByStationSlotPositionAndStationStationId(Integer stationSlotPosition, Long stationStationId);

}
