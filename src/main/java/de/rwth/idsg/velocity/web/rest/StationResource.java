package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.repository.StationRepository;
import de.rwth.idsg.velocity.service.StationService;
import de.rwth.idsg.velocity.web.rest.dto.StationDTO;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for managing Station.
 */
@RestController
@RequestMapping("/app")
public class StationResource {

    private final Logger log = LoggerFactory.getLogger(StationResource.class);

    @Autowired
    private StationRepository stationRepository;


    /**
     * POST  /rest/stations -> Create a new station.
     */
    @RequestMapping(value = "/rest/stations",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
//    public void create(@RequestBody Station station) {
    public void create(@Valid @RequestBody CreateEditStationDTO dto) {
        log.debug("REST request to save Station : {}", dto);
        stationRepository.create(dto);
    }

    /**
     * GET  /rest/stations -> get all the stations.
     */
    @RequestMapping(value = "/rest/stations",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<ViewStationDTO> getAll() {
        log.info("REST request to get all Stations");
        List<ViewStationDTO> stations = stationRepository.findAll();
        log.info("List: {}", stations);
        return stations;
    }

    /**
     * GET  /rest/stations/:id -> get the "id" station.
     */
    @RequestMapping(value = "/rest/stations/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public Station get(@PathVariable Long id, HttpServletResponse response) {
        log.info("REST request to get Station : {}", id);
        Station station = stationRepository.findOne(id);
        if (station == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return station;
    }

    /**
     * DELETE  /rest/stations/:id -> delete the "id" station.
     */
    @RequestMapping(value = "/rest/stations/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to delete Station : {}", id);
        stationRepository.delete(id);
    }
}
