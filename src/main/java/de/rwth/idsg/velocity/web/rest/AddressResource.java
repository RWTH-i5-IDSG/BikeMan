package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.domain.Address;
import de.rwth.idsg.velocity.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Address.
 */
@RestController
@RequestMapping("/app")
public class AddressResource {

    private final Logger log = LoggerFactory.getLogger(AddressResource.class);

    @Inject
    private AddressRepository addressRepository;

    /**
     * POST  /rest/addresss -> Create a new address.
     */
    @RequestMapping(value = "/rest/addresss",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void create(@RequestBody Address address) {
        log.debug("REST request to save Address : {}", address);
        addressRepository.save(address);
    }

    /**
     * GET  /rest/addresss -> get all the addresss.
     */
    @RequestMapping(value = "/rest/addresss",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<Address> getAll() {
        log.debug("REST request to get all Addresss");
        return addressRepository.findAll();
    }

    /**
     * GET  /rest/addresss/:id -> get the "id" address.
     */
    @RequestMapping(value = "/rest/addresss/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public Address get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Address : {}", id);
        Address address = addressRepository.findOne(id);
        if (address == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return address;
    }

    /**
     * DELETE  /rest/addresss/:id -> delete the "id" address.
     */
    @RequestMapping(value = "/rest/addresss/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to delete Address : {}", id);
        addressRepository.delete(id);
    }
}
