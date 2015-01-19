package de.rwth.idsg.bikeman.web.rest;

/**
 * Created by Wolfgang Kluth on 19/01/15.
 */

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.domain.TariffType;
import de.rwth.idsg.bikeman.repository.TariffRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST controller for managing Tariffs.
 */
@RestController
@RequestMapping("/app")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class TariffResource {

    @Autowired
    private TariffRepository tariffRepository;

    private static final String BASE_PATH = "/rest/tariffs";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<TariffType> getAll() throws DatabaseException {
        log.debug("REST request to get all Tariffs");
        return tariffRepository.findAllNames();
    }

}
