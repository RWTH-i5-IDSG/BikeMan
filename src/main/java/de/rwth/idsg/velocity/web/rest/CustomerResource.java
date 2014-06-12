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
    private static final String ID_PATH = "/rest/customers/{id}";
    private static final String FULL_NAME_PATH = "/rest/customers/name/{firstname}+{lastname}";

    // Restriction: Customers login with an e-mail address
    // Regular expression for Spring MVC to interpret domain extensions as part of the path variable
    private static final String LOGIN_PATH = "/rest/customers/login/{login:.+}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewCustomerDTO> getAll(HttpServletResponse response) {
        log.debug("REST request to get all customers");
        List<ViewCustomerDTO> answer = null;
        try {
            answer = customerRepository.findAll();
        } catch (Exception e) {
            log.error("Error occurred.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return answer;
    }

    @Timed
    @RequestMapping(value = FULL_NAME_PATH, method = RequestMethod.GET)
    public List<ViewCustomerDTO> getByName(@PathVariable String firstname, @PathVariable String lastname, HttpServletResponse response) {
        log.debug("REST request to get Customer with name: {} {}", firstname, lastname);
        List<ViewCustomerDTO> answer = null;
        try {
            answer = customerRepository.findbyName(firstname,lastname);
            if (answer.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error occurred.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return answer;
    }

    @Timed
    @RequestMapping(value = LOGIN_PATH, method = RequestMethod.GET)
    public ViewCustomerDTO getByLogin(@PathVariable String login, HttpServletResponse response) {
        log.debug("REST request to get Customer with login: {}", login);
        ViewCustomerDTO answer = null;
        try {
            answer = customerRepository.findbyLogin(login);
            if (answer == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error occurred.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return answer;
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public void create(@Valid @RequestBody CreateEditCustomerDTO dto, HttpServletResponse response) {
        log.debug("REST request to create Customer : {}", dto);
        try {
            customerRepository.create(dto);
        } catch (Exception e) {
            log.error("Error occurred.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.PUT)
    public void update(@Valid @RequestBody CreateEditCustomerDTO dto, HttpServletResponse response) {
        log.debug("REST request to update Customer : {}", dto);
        try {
            customerRepository.update(dto);
        } catch (Exception e) {
            log.error("Error occurred.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Timed
    @RequestMapping(value = ID_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to delete Customer : {}", id);
        try {
            customerRepository.delete(id);
        } catch (Exception e) {
            log.error("Error occurred.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}