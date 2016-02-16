package de.rwth.idsg.bikeman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.service.StationService;
import de.rwth.idsg.bikeman.web.rest.dto.modify.ChangeStationOperationStateDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.StationConfigurationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewErrorDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for managing Station.
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class StationResource {

    @Autowired
    private StationService stationService;

    private static final String BASE_PATH = "/stations";
    private static final String ID_PATH = "/stations/{id}";
    private static final String ERROR_PATH = "/stations/errors";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditStationDTO dto) throws DatabaseException {
        log.debug("REST request to save Station : {}", dto);
        stationService.create(dto);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.PUT)
    public void update(@Valid @RequestBody CreateEditStationDTO dto) throws DatabaseException {
        log.debug("REST request to update Station : {}", dto);
        stationService.updateStation(dto);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewStationDTO> getAll() throws DatabaseException {
        log.debug("REST request to get all Stations");
        return stationService.getAll();
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.GET)
    public ViewStationDTO get(@PathVariable Long id) throws DatabaseException {
        log.info("REST request to get Station : {}", id);
        return stationService.get(id);
    }

//    @Timed
//    @RequestMapping(value = ID_PATH, method = RequestMethod.DELETE)
//    public void delete(@PathVariable Long id) throws DatabaseException {
//        log.debug("REST request to delete Station : {}", id);
//        stationRepository.delete(id);
//    }

    @Timed
    @RequestMapping(value = ID_PATH + "/slotState", method = RequestMethod.POST)
    public void changeSlotState(@PathVariable Long id, @Valid @RequestBody ChangeStationOperationStateDTO dto) throws DatabaseException {
        log.debug("REST request to change slot state: {}", dto);
        stationService.changeSlotState(id, dto);
    }

    @Timed
    @RequestMapping(value = ID_PATH + "/reboot", method = RequestMethod.POST)
    public void reboot(@PathVariable Long id) throws DatabaseException {
        log.debug("REST request to reboot station {}", id);
        stationService.reboot(id);
    }

    @Timed
    @RequestMapping(value = ID_PATH + "/config", method = RequestMethod.GET)
    public StationConfigurationDTO getConfig(@PathVariable Long id) throws DatabaseException {
        log.debug("REST request to get station configuration for station: {}", id);
        return stationService.getConfig(id);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.POST)
    public void updateConfig(@PathVariable Long id, @Valid @RequestBody StationConfigurationDTO dto) throws DatabaseException {
        log.debug("REST request to change station configuration: {}", dto);
        stationService.updateConfig(id, dto);
    }

    @Timed
    @RequestMapping(value = ID_PATH + "/unlockSlot/{slotId}", method = RequestMethod.POST)
    public void unlockSlot(@PathVariable("id") Long stationId, @PathVariable("slotId") Long slotId) {
        stationService.unlockSlot(stationId, slotId);
    }

    @Timed
    @RequestMapping(value = ERROR_PATH, method = RequestMethod.GET)
    public List<ViewErrorDTO> getErrors() {
        log.debug("REST request to get station and stationSlot errors.");
        return stationService.getErrors();
    }
}
