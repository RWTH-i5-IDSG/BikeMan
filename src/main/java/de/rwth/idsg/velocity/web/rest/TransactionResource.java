package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.repository.TransactionRepository;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewTransactionDTO;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
@Slf4j
public class TransactionResource {

    @Inject
    private TransactionRepository transactionRepository;

    private static final String BASE_PATH = "/rest/transactions";
    private static final String BASE_PATH_OPEN = "/rest/transactions/open";
    private static final String BASE_PATH_CLOSED = "/rest/transactions/closed";
    private static final String PEDELEC_ID_PATH = "/rest/transactions/pedelec/{pedelecId}";
    private static final String CUSTOMER_LOGIN_PATH = "/rest/transactions/customer/{login:.+}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getAll() throws DatabaseException {
        log.debug("REST request to get all Transactions");
        return transactionRepository.findAll();
    }

    @Timed
    @RequestMapping(value = PEDELEC_ID_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getByPedelecId(@PathVariable Long pedelecId, @RequestParam(required = false) Integer resultSize) throws DatabaseException {
        log.debug("REST request to get last {} transactions for pedelec with pedelecId {}", resultSize, pedelecId);
        return transactionRepository.findByPedelecId(pedelecId, resultSize);
    }

    @Timed
    @RequestMapping(value = CUSTOMER_LOGIN_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getByLogin(@PathVariable String login, @RequestParam(required = false) Integer resultSize) throws DatabaseException {
        log.debug("REST request to get last {} transactions for user with login {}", resultSize, login);
        return transactionRepository.findByLogin(login, resultSize);
    }

    @Timed
    @RequestMapping(value = BASE_PATH_OPEN, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getOpen() throws DatabaseException {
        log.debug("REST request to get open Transactions");
        return transactionRepository.findOpen();
    }

    @Timed
    @RequestMapping(value = BASE_PATH_CLOSED, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getClosed() throws DatabaseException {
        log.debug("REST request to get closed Transactions");
        return transactionRepository.findClosed();
    }
}
