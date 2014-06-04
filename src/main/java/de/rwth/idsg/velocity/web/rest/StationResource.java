package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.repository.StationRepository;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST controller for managing Station.
 */
@RestController
@RequestMapping("/app")
@Produces(MediaType.APPLICATION_JSON)
public class StationResource {

    private static final Logger log = LoggerFactory.getLogger(StationResource.class);

    @Autowired
    private StationRepository stationRepository;

    private static final String BASE_PATH = "/rest/stations";
    private static final String ID_PATH = "/rest/stations/{id}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditStationDTO dto) {
        log.debug("REST request to save Station : {}", dto);
        stationRepository.create(dto);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.PUT)
    public void update(@Valid @RequestBody CreateEditStationDTO dto) {
        log.debug("REST request to save Station : {}", dto);
        stationRepository.update(dto);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewStationDTO> getAll() {
        log.info("REST request to get all Stations");
        return stationRepository.findAll();
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.GET)
    public ViewStationDTO get(@PathVariable Long id, HttpServletResponse response) {
        log.info("REST request to get Station : {}", id);
        ViewStationDTO station = stationRepository.findOne(id);
        if (station == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return station;
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to delete Station : {}", id);
        stationRepository.delete(id);
    }
}
