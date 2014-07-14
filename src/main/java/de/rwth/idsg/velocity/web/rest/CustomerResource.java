package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.repository.CustomerRepository;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditCustomerDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewCustomerDTO;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/app")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class CustomerResource {

    @Inject
    private CustomerRepository customerRepository;

    private static final String BASE_PATH = "/rest/customers";
    private static final String ID_PATH = "/rest/customers/{id}";
    private static final String ID_PATH_ACTIVATE = "/rest/customers/{id}/activate";
    private static final String ID_PATH_DEACTIVATE = "/rest/customers/{id}/deactivate";
    private static final String FULL_NAME_PATH = "/rest/customers/name/{firstname}+{lastname}";

    // Restriction: Customers login with an e-mail address
    // Regular expression for Spring MVC to interpret domain extensions as part of the path variable
    private static final String LOGIN_PATH = "/rest/customers/login/{login:.+}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewCustomerDTO> getAll() throws DatabaseException {
        log.debug("REST request to get all customers");
        return customerRepository.findAll();
    }

    @Timed
    @RequestMapping(value = FULL_NAME_PATH, method = RequestMethod.GET)
    public List<ViewCustomerDTO> getByName(@PathVariable String firstname, @PathVariable String lastname) throws DatabaseException {
        log.debug("REST request to get Customer with name: {} {}", firstname, lastname);
        return customerRepository.findbyName(firstname,lastname);
    }

    @Timed
    @RequestMapping(value = LOGIN_PATH, method = RequestMethod.GET)
    public ViewCustomerDTO getByLogin(@PathVariable String login) throws DatabaseException {
        log.debug("REST request to get Customer with login: {}", login);
        return customerRepository.findbyLogin(login);
    }

    @Timed
    @RequestMapping(value = ID_PATH_ACTIVATE, method = RequestMethod.PUT)
    public void activate(@PathVariable Long id) throws DatabaseException {
        log.debug("REST request to activate Customer with id: {}", id);
        customerRepository.activate(id);
    }

    @Timed
    @RequestMapping(value = ID_PATH_DEACTIVATE, method = RequestMethod.PUT)
    public void deactivate(@PathVariable Long id) throws DatabaseException {
        log.debug("REST request to deactivate Customer with id: {}", id);
        customerRepository.deactivate(id);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditCustomerDTO dto) throws DatabaseException {
        log.debug("REST request to create Customer : {}", dto);
        customerRepository.create(dto);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.PUT)
    public void update(@Valid @RequestBody CreateEditCustomerDTO dto) throws DatabaseException {
        log.debug("REST request to update Customer : {}", dto);
        customerRepository.update(dto);
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) throws DatabaseException {
        log.debug("REST request to delete Customer : {}", id);
        customerRepository.delete(id);
    }
}