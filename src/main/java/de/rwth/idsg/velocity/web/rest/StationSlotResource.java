package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.domain.StationSlot;
import de.rwth.idsg.velocity.repository.StationSlotRepository;
import de.rwth.idsg.velocity.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing StationSlot.
 */
@RestController
@RequestMapping("/app")
public class StationSlotResource {

    private final Logger log = LoggerFactory.getLogger(StationSlotResource.class);

    @Inject
    private StationSlotRepository stationslotRepository;

    /**
     * POST  /rest/stationslots -> Create a new stationslot.
     */
    @RequestMapping(value = "/rest/stationslots",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void create(@RequestBody StationSlot stationslot) {
        log.debug("REST request to save StationSlot : {}", stationslot);
        stationslotRepository.save(stationslot);
    }

    /**
     * GET  /rest/stationslots -> get all the stationslots.
     */
    @RequestMapping(value = "/rest/stationslots",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<StationSlot> getAll() {
        log.debug("REST request to get all StationSlots");
        return stationslotRepository.findAll();
    }

    /**
     * GET  /rest/stationslots/:id -> get the "id" stationslot.
     */
    @RequestMapping(value = "/rest/stationslots/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public StationSlot get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get StationSlot : {}", id);
        StationSlot stationslot = stationslotRepository.findOne(id);
        if (stationslot == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return stationslot;
    }

    /**
     * DELETE  /rest/stationslots/:id -> delete the "id" stationslot.
     */
    @RequestMapping(value = "/rest/stationslots/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to delete StationSlot : {}", id);
        stationslotRepository.delete(id);
    }

    /**
     * POST /rest/stationslots/setState/:id -> Set state of "id" stationslot.
     */
    @RolesAllowed(AuthoritiesConstants.MANAGER)
    @RequestMapping(value = "/rest/stationslots/setState",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void changeRentState(@PathVariable long id, @RequestParam Boolean state) {
        StationSlot stationSlot = stationslotRepository.findOne(id);
        stationSlot.setState(state);
        stationslotRepository.save(stationSlot);
    }
}
