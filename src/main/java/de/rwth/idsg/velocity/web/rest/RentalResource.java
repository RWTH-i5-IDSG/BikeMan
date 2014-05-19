package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.service.RentalService;
import de.rwth.idsg.velocity.web.rest.dto.ReturnPedelecDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.concurrent.Callable;

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
     * POST  current user pick up pedelec at slot with id
     */
    @RequestMapping(value = "/rest/rental/pickUp/{stationSlotId}",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public Callable<Void> pickUp(@PathVariable final long stationSlotId, final HttpServletResponse response) {
        log.debug("REST request to execute Rental: {}", stationSlotId);

        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                if (rentalService.pickUp(stationSlotId)) {
                    log.debug("ok");
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    log.debug("error");
                    response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
                }
                return null;
            }
        };
    }

    /**
     * POST  pedelec returned to slot with slot id
     */
    @RequestMapping(value = "/rest/rental/returnPedelec",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void returnPedelec(@RequestBody ReturnPedelecDTO returnPedelecDTO) {

        rentalService.returnPedelec(returnPedelecDTO.getPedelecId(), returnPedelecDTO.getStationSlotId());
    }

}
