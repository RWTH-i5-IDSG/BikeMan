package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.repository.StationRepository;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * REST controller for managing Station.
 */
@RestController
@RequestMapping("/app")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class StationResource {

    @Autowired
    private StationRepository stationRepository;

    private static final String BASE_PATH = "/rest/stations";
    private static final String ID_PATH = "/rest/stations/{id}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditStationDTO dto) throws BackendException {
        log.debug("REST request to save Station : {}", dto);
        stationRepository.create(dto);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.PUT)
    public void update(@Valid @RequestBody CreateEditStationDTO dto) throws BackendException {
        log.debug("REST request to update Station : {}", dto);
        stationRepository.update(dto);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewStationDTO> getAll() throws BackendException {
        log.info("REST request to get all Stations");
        return stationRepository.findAll();
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.GET)
    public ViewStationDTO get(@PathVariable Long id) throws BackendException {
        log.info("REST request to get Station : {}", id);
        return stationRepository.findOne(id);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) throws BackendException {
        log.debug("REST request to delete Station : {}", id);
        stationRepository.delete(id);
    }

    ///// Methods to catch exceptions /////

    @ExceptionHandler(BackendException.class)
    public void backendConflict(HttpServletResponse response, BackendException e) throws IOException {
        log.error("Exception happened", e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void conflict(HttpServletResponse response, Exception e) {
        log.error("Exception happened", e);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}