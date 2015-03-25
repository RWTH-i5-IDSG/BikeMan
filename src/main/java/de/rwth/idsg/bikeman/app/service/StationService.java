package de.rwth.idsg.bikeman.app.service;


import de.rwth.idsg.bikeman.app.dto.ViewStationDTO;
import de.rwth.idsg.bikeman.app.dto.ViewStationSlotsDTO;
import de.rwth.idsg.bikeman.app.repository.StationRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("StationServiceApp")
@Slf4j
public class StationService {

    @Autowired
    private StationRepository stationRepository;


    public List<ViewStationDTO> getAll() throws DatabaseException {
        return stationRepository.findAll();
    }

    public ViewStationDTO get(Long id) throws DatabaseException {
        return stationRepository.findOne(id);
    }

    public List<ViewStationSlotsDTO> getSlots(Long id) throws DatabaseException {
        return stationRepository.findOneWithSlots(id);
    }

}