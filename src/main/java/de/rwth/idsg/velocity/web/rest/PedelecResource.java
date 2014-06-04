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
    public List<ViewPedelecDTO> getAll() {
        log.info("REST request to get all Pedelecs");
        return pedelecRepository.findAll();
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.GET)
    public ViewPedelecDTO get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Pedelec : {}", id);
        ViewPedelecDTO pedelec = pedelecRepository.findOneDTO(id);
        if (pedelec == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return pedelec;
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditPedelecDTO pedelec) {
        log.debug("REST request to save Pedelec : {}", pedelec);
        pedelecRepository.create(pedelec);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.PUT)
    public void update(@PathVariable Long id, @Valid @RequestBody CreateEditPedelecDTO pedelec) {
        log.debug("REST request to update Pedelec : {}", id);
        pedelecRepository.update(pedelec);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to delete Pedelec : {}", id);
        pedelecRepository.delete(id);
    }
}
