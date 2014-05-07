package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.domain.Customer;
import de.rwth.idsg.velocity.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/app")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    @Inject
    private CustomerRepository customerRepository;

    /**
     * POST  /rest/customers -> Create a new customer.
     */
    @RequestMapping(value = "/rest/customers",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void create(@RequestBody Customer customer) {
        log.debug("REST request to save Customer : {}", customer);
        customerRepository.save(customer);
    }

    /**
     * GET  /rest/customers -> get all the customers.
     */
    @RequestMapping(value = "/rest/customers",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<Customer> getAll() {
        log.debug("REST request to get all Customers");
        return customerRepository.findAll();
    }

    /**
     * GET  /rest/customers/:id -> get the "id" customer.
     */
    @RequestMapping(value = "/rest/customers/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public Customer get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Customer : {}", id);
        Customer customer = customerRepository.findOne(id);
        if (customer == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return customer;
    }

    /**
     * DELETE  /rest/customers/:id -> delete the "id" customer.
     */
    @RequestMapping(value = "/rest/customers/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to delete Customer : {}", id);
        customerRepository.delete(id);
    }
}
