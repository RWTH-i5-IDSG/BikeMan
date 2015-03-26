package de.rwth.idsg.bikeman.app.resource;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.app.dto.ViewStationDTO;
import de.rwth.idsg.bikeman.app.dto.ViewStationSlotsDTO;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.app.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@RestController("StationResourceApp")
// TODO: change mapping path
@RequestMapping("/apptest")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class StationResource {

    @Autowired
    private StationService stationService;

    private static final String BASE_PATH = "/stations";
    private static final String ID_PATH = "/stations/{id}";
    private static final String SLOT_PATH = "/stations/{id}/slots";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewStationDTO> getAll() throws AppException {
        log.debug("REST request to get all Stations");
        return stationService.getAll();
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.GET)
    public ViewStationDTO get(@PathVariable Long id) throws AppException {
        log.info("REST request to get Station : {}", id);
        return stationService.get(id);
    }

    @Timed
    @RequestMapping(value = SLOT_PATH, method = RequestMethod.GET)
    public List<ViewStationSlotsDTO> getSlots(@PathVariable Long id) throws AppException {
        log.info("REST request to get Slots of Station : {}", id);
        return stationService.getSlots(id);
    }

}
