package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.repository.PedelecRepository;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewPedelecDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * REST controller for managing Pedelec.
 */
@RestController
@RequestMapping("/app")
@Produces(MediaType.APPLICATION_JSON)
public class PedelecResource {

    private static final Logger log = LoggerFactory.getLogger(PedelecResource.class);

    @Autowired
    private PedelecRepository pedelecRepository;

    private static final String BASE_PATH = "/rest/pedelecs";
    private static final String ID_PATH = "/rest/pedelecs/{id}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewPedelecDTO> getAll() throws BackendException {
        log.info("REST request to get all Pedelecs");
        return pedelecRepository.findAll();
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.GET)
    public ViewPedelecDTO get(@PathVariable Long id) throws BackendException {
        log.debug("REST request to get Pedelec : {}", id);
        return pedelecRepository.findOneDTO(id);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditPedelecDTO pedelec) throws BackendException {
        log.debug("REST request to save Pedelec : {}", pedelec);
        pedelecRepository.create(pedelec);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.PUT)
    public void update(@Valid @RequestBody CreateEditPedelecDTO dto) throws BackendException {
        log.debug("REST request to update Pedelec : {}", dto);
        pedelecRepository.update(dto);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) throws BackendException {
        log.debug("REST request to delete Pedelec : {}", id);
        pedelecRepository.delete(id);
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