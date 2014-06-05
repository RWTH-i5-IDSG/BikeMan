package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.repository.CustomerRepository;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditCustomerDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewCustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
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
public class CustomerResource {

    private static final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    @Inject
    private CustomerRepository customerRepository;

    private static final String BASE_PATH = "/rest/customers";
    private static final String FULL_NAME_PATH = "/rest/customers/name/{firstname}+{lastname}";
    private static final String EMAIL_PATH = "/rest/customers/email/{email}";
    private static final String LOGIN_PATH = "/rest/customers/login/{login}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewCustomerDTO> getAll() {
        log.debug("REST request to get all customers");
        return customerRepository.findAll();
    }

    @Timed
    @RequestMapping(value = FULL_NAME_PATH, method = RequestMethod.GET)
    public List<ViewCustomerDTO> getByName(@PathVariable String firstname, @PathVariable String lastname, HttpServletResponse response) {
        log.debug("REST request to get Customer with name: {} {}", firstname, lastname);
        List<ViewCustomerDTO> list = customerRepository.findbyName(firstname,lastname);
        if (list.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return list;
    }

    @Timed
    @RequestMapping(value = EMAIL_PATH, method = RequestMethod.GET)
    public ViewCustomerDTO getByEmail(@PathVariable String email, HttpServletResponse response) {
        log.debug("REST request to get Customer with email: {}", email);
        ViewCustomerDTO customer = customerRepository.findbyEmail(email);
        if (customer == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return customer;
    }

    @Timed
    @RequestMapping(value = LOGIN_PATH, method = RequestMethod.GET)
    public ViewCustomerDTO getByLogin(@PathVariable String login, HttpServletResponse response) {
        log.debug("REST request to get Customer with login: {}", login);
        ViewCustomerDTO customer = customerRepository.findbyLogin(login);
        if (customer == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return customer;
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditCustomerDTO dto) {
        log.debug("REST request to create Customer : {}", dto);
        customerRepository.create(dto);
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.PUT)
    public void update(@Valid @RequestBody CreateEditCustomerDTO dto) {
        log.debug("REST request to update Customer : {}", dto);
        customerRepository.update(dto);
    }

    @Timed
    @RequestMapping(value = LOGIN_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable String login, HttpServletResponse response) {
        log.debug("REST request to delete Customer : {}", login);
        customerRepository.delete(login);
    }
}