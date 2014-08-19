package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.repository.PedelecRepository;
import de.rwth.idsg.velocity.service.PedelecService;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.velocity.web.rest.dto.modify.PedelecConfigurationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewPedelecDTO;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

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
@Slf4j
public class PedelecResource {

    @Autowired
    private PedelecRepository pedelecRepository;

    @Autowired
    private PedelecService pedelecService;

    private static final String BASE_PATH = "/rest/pedelecs";
    private static final String ID_PATH = "/rest/pedelecs/{id}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewPedelecDTO> getAll() throws DatabaseException {
        log.info("REST request to get all Pedelecs");
        return pedelecRepository.findAll();
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.GET)
    public ViewPedelecDTO get(@PathVariable Long id) throws DatabaseException {
        log.debug("REST request to get Pedelec : {}", id);
        return pedelecRepository.findOneDTO(id);
    }

    @Timed
    @RequestMapping(value = ID_PATH + "/config", method = RequestMethod.GET)
    public PedelecConfigurationDTO getConfig(@PathVariable Long id) throws DatabaseException, RestClientException {
        log.debug("REST request to get station configuration for station: {}", id);

        return pedelecService.getPedelecConfig(id);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.POST)
    public void updateConfig(@PathVariable Long id, @Valid @RequestBody PedelecConfigurationDTO dto) throws DatabaseException, RestClientException {
        log.debug("REST request to change station configuration: {}", dto);

        pedelecService.changePedelecConfiguration(id, dto);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditPedelecDTO pedelec) throws DatabaseException {
        log.debug("REST request to save Pedelec : {}", pedelec);
        pedelecRepository.create(pedelec);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.PUT)
    public void update(@Valid @RequestBody CreateEditPedelecDTO dto) throws DatabaseException, RestClientException {
        log.debug("REST request to update Pedelec : {}", dto);

        pedelecService.changeOperationState(dto);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) throws DatabaseException {
        log.debug("REST request to delete Pedelec : {}", id);
        pedelecRepository.delete(id);
    }
}