package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.service.RentalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * REST controller for managing Rental Actions.
 */
@RestController
@RequestMapping("/app")
public class RentalResource {

    @Inject
    private RentalService rentalService;

    private final Logger log = LoggerFactory.getLogger(PedelecResource.class);

    /**
     * POST  /rest/pedelecs -> Create a new pedelec.
     */
    @RequestMapping(value = "/rest/rental/pickUp/{stationSlotId}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public void create(@PathVariable long stationSlotId) {
        log.debug("REST request to execute Rental: {}", stationSlotId);

        if (rentalService.pickUp(stationSlotId)) {
            log.debug("ok");
        } else {
            log.debug("error");
        }
    }
}
