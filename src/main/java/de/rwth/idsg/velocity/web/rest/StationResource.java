package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.repository.StationRepository;
import de.rwth.idsg.velocity.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Station.
 */
@RestController
@RequestMapping("/app")
public class StationResource {

    private final Logger log = LoggerFactory.getLogger(StationResource.class);

    @Inject
    private StationRepository stationRepository;

    /**
     * POST  /rest/stations -> Create a new station.
     */
    @RequestMapping(value = "/rest/stations",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void create(@RequestBody Station station) {
        log.debug("REST request to save Station : {}", station);
        stationRepository.save(station);
    }

    /**
     * GET  /rest/stations -> get all the stations.
     */
    @RequestMapping(value = "/rest/stations",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<Station> getAll() {
        log.debug("REST request to get all Stations");
        return stationRepository.findAll();
    }

    /**
     * GET  /rest/stations/:id -> get the "id" station.
     */
    @RequestMapping(value = "/rest/stations/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public Station get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Station : {}", id);
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

    /**
     * POST /rest/stations/setState/:id -> Set state of "id" station.
     */
    @RolesAllowed(AuthoritiesConstants.MANAGER)
    @RequestMapping(value = "/rest/stations/setAvailability",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void changeRentState(@PathVariable long id, @RequestParam Boolean state) {
        Station station = stationRepository.findOne(id);
        station.setState(state);
        stationRepository.save(station);
    }
}
