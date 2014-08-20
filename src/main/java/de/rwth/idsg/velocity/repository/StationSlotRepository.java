package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.StationSlot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by swam on 18/06/14.
 */
public interface StationSlotRepository extends JpaRepository<StationSlot, Long> {

    public StationSlot findByStationSlotPositionAndStationStationId(Integer stationSlotPosition, Long stationStationId);

}
