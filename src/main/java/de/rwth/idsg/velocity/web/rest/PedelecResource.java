package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.repository.PedelecRepository;
import de.rwth.idsg.velocity.service.PedelecService;
import de.rwth.idsg.velocity.web.rest.dto.PedelecDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Pedelec.
 */
@RestController
@RequestMapping("/app")
public class PedelecResource {

    private final Logger log = LoggerFactory.getLogger(PedelecResource.class);

    @Autowired
    private PedelecRepository pedelecRepository;

    @Inject
    private PedelecService pedelecService;

    /**
     * POST  /rest/pedelecs -> Create a new pedelec.
     */
    @RequestMapping(value = "/rest/pedelecs",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
//    public void create(@RequestBody Pedelec pedelec) {
    public void create(@RequestBody PedelecDTO pedelec) {
        log.debug("REST request to save Pedelec : {}", pedelec);
        pedelecService.createPedelec(pedelec);
    }

    /**
     * GET  /rest/pedelecs -> get all the pedelecs.
     */
    @RequestMapping(value = "/rest/pedelecs",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<Pedelec> getAll() {
        log.debug("REST request to get all Pedelecs");
        return pedelecRepository.findAll();
    }

    /**
     * GET  /rest/pedelecs/:id -> get the "id" pedelec.
     */
    @RequestMapping(value = "/rest/pedelecs/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public Pedelec get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Pedelec : {}", id);
        Pedelec pedelec = pedelecRepository.findOne(id);
        if (pedelec == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return pedelec;
    }

    /**
     * DELETE  /rest/pedelecs/:id -> delete the "id" pedelec.
     */
    @RequestMapping(value = "/rest/pedelecs/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to delete Pedelec : {}", id);
        pedelecRepository.delete(id);
    }
}
