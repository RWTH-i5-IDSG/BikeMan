package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.repository.TransactionRepository;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewTransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST controller for managing Transaction.
 */
@RestController
@RequestMapping("/app")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    @Inject
    private TransactionRepository transactionRepository;

    private static final String BASE_PATH = "/rest/transactions";
    private static final String BASE_PATH_OPEN = "/rest/transactions/open";  // This is new (SG)

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getAll() {
        log.debug("REST request to get all Transactions");
        List<ViewTransactionDTO> list = transactionRepository.findAll();
        log.debug("List with size {}: {}", list.size(), list);
        return list;
    }

    @Timed
    @RequestMapping(value = BASE_PATH_OPEN, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getOpen() {
        log.debug("REST request to get open Transactions");
        List<ViewTransactionDTO> list = transactionRepository.findOpen();
        log.debug("List with size {}: {}", list.size(), list);
        return list;
    }

}
