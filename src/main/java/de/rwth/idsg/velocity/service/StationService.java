package de.rwth.idsg.velocity.service;

import com.google.common.collect.Multiset;
import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.domain.StationSlot;
import de.rwth.idsg.velocity.repository.AddressRepository;
import de.rwth.idsg.velocity.repository.StationRepository;
import de.rwth.idsg.velocity.web.rest.dto.StationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by max on 12/05/14.
 */
@Service
@Transactional
public class StationService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private StationRepository stationRepository;

    @Inject
    private AddressRepository addressRepository;

    public void createStation(StationDTO station) {
        Station newStation = new Station();

        newStation.setName(station.getName());
        newStation.setNote("");
        newStation.setAddress(station.getAddress());
        newStation.setLocationLatitude(station.getLocationLatitude());
        newStation.setLocationLongitude(station.getLocationLongitude());

        Set<StationSlot> stationSlots = new HashSet<>();
        int count = station.getNumberOfSlots();
        for (int i = 0; i < count; i++) {
            StationSlot slot = new StationSlot();
            slot.setStation(newStation);

            stationSlots.add(slot);
        }
        newStation.setStationSlots(stationSlots);

        stationRepository.save(newStation);

        log.info("created new station: {}", newStation);
    }
}
