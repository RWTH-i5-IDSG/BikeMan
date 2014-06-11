package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.domain.Manager;
import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.repository.ManagerRepository;
import de.rwth.idsg.velocity.repository.PedelecRepository;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateManagerDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewManagerDTO;
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
public class ManagerResource {

    private static final Logger log = LoggerFactory.getLogger(PedelecResource.class);

    @Autowired
    private ManagerRepository managerRepository;

    private static final String BASE_PATH = "/rest/managers";
    private static final String ID_PATH = "/rest/managers/{id}";

    // Restriction: Customers login with an e-mail address
    // Regular expression for Spring MVC to interpret domain extensions as part of the path variable
    private static final String LOGIN_PATH = "/rest/managers/{login:+}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewManagerDTO> getAll() {
        log.info("REST request to get all Pedelecs");
        return managerRepository.findAll();
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateManagerDTO manager) {
        log.debug("REST request to save manager : {}", manager);
        managerRepository.create(manager);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.PUT)
    public void update(@PathVariable Long id, @Valid @RequestBody Manager manager) {
        log.debug("REST request to update Manager : {}", id);
        managerRepository.update(manager);
    }

    @Timed
    @RequestMapping(value = LOGIN_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable String login, HttpServletResponse response) {
        log.debug("REST request to delete Manager : {}", login);
        managerRepository.delete(login);
    }
}
