package de.rwth.idsg.bikeman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.repository.ManagerRepository;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditManagerDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewManagerDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for managing Pedelec.
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ManagerResource {

    @Autowired
    private ManagerRepository managerRepository;

    private static final String BASE_PATH = "/managers";
    private static final String ID_PATH = "/managers/{id}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewManagerDTO> getAll() throws DatabaseException {
        log.info("REST request to get all Manager");
        return managerRepository.findAll();
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditManagerDTO manager) throws DatabaseException {
        log.debug("REST request to save manager : {}", manager);
        managerRepository.create(manager);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.PUT)
    public void update(@Valid @RequestBody CreateEditManagerDTO manager) throws DatabaseException {
        log.debug("REST request to update Manager");
        managerRepository.update(manager);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) throws DatabaseException {
        log.debug("REST request to delete Manager : {}", id);
        managerRepository.delete(id);
    }
}
