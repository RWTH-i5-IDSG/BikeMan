package de.rwth.idsg.bikeman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.service.PedelecService;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.PedelecConfigurationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewErrorDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for managing Pedelec.
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PedelecResource {

    @Autowired
    private PedelecService pedelecService;

    private static final String BASE_PATH = "/pedelecs";
    private static final String ID_PATH = "/pedelecs/{id}";
    private static final String CONFIG_PATH = "/pedelecs/{id}/config";
    private static final String ERROR_PATH = "/pedelecs/errors";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewPedelecDTO> getAll() throws DatabaseException {
        log.debug("REST request to get all Pedelecs");
        return pedelecService.getAll();
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.GET)
    public ViewPedelecDTO get(@PathVariable Long id) throws DatabaseException {
        log.debug("REST request to get Pedelec : {}", id);
        return pedelecService.get(id);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditPedelecDTO pedelec) throws DatabaseException {
        log.debug("REST request to save Pedelec : {}", pedelec);
        pedelecService.create(pedelec);
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
        pedelecService.delete(id);
    }

    @Timed
    @RequestMapping(value = CONFIG_PATH, method = RequestMethod.GET)
    public PedelecConfigurationDTO getConfig(@PathVariable Long id) throws DatabaseException, RestClientException {
        log.debug("REST request to get station configuration for station: {}", id);
        return pedelecService.getConfig(id);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.POST)
    public void updateConfig(@PathVariable Long id, @Valid @RequestBody PedelecConfigurationDTO dto) throws DatabaseException, RestClientException {
        log.debug("REST request to change station configuration: {}", dto);
        pedelecService.changeConfig(id, dto);
    }

    @Timed
    @RequestMapping(value = ERROR_PATH, method = RequestMethod.GET)
    public List<ViewErrorDTO> getErrors() throws DatabaseException {
        log.debug("REST request to get station errors");
        return pedelecService.getErrors();
    }
}
