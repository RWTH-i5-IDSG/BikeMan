package de.rwth.idsg.bikeman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.repository.MajorCustomerRepository;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditMajorCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewMajorCustomerDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by swam on 17/10/14.
 */

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MajorCustomerResource {

    @Inject
    private MajorCustomerRepository majorCustomerRepository;

    private static final String BASE_PATH = "/majorcustomers";
    private static final String ID_PATH = "/majorcustomers/{login:.+}";
    private static final String CARD_ACCOUNT_PATH = "/majorcustomers/{login.+}/card-accounts";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewMajorCustomerDTO> getAll() throws DatabaseException {
        log.debug("REST Request to get all majorcustomers");
        return majorCustomerRepository.findAll();
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.GET)
    public ViewMajorCustomerDTO getOne(@PathVariable String login) throws DatabaseException {
        log.debug("REST request to get Customer with login: {}", login);
        return majorCustomerRepository.findByLogin(login);
    }

    @Timed
    @RequestMapping(value = CARD_ACCOUNT_PATH, method = RequestMethod.GET)
    public ViewMajorCustomerDTO getCardAccounts() throws DatabaseException {
        log.debug("REST request to get majorcustomers cardAccounts");
        return null;
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditMajorCustomerDTO dto) throws DatabaseException {
        log.debug("REST request to create Customer : {}", dto);
        majorCustomerRepository.create(dto);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.PUT)
    public void update(@Valid @RequestBody CreateEditMajorCustomerDTO dto) throws DatabaseException {
        log.debug("REST request to update Customer : {}", dto);
        majorCustomerRepository.update(dto);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) throws DatabaseException {
        log.debug("REST request to delete Customer : {}", id);
        majorCustomerRepository.delete(id);
    }
}
