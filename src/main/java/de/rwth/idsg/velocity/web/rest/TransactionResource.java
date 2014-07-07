package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.repository.TransactionRepository;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewTransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
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
    private static final String BASE_PATH_OPEN = "/rest/transactions/open";
    private static final String BASE_PATH_CLOSED = "/rest/transactions/closed";
    private static final String PEDELEC_ID_PATH = "/rest/transactions/pedelec/{pedelecId}";
    private static final String CUSTOMER_LOGIN_PATH = "/rest/transactions/customer/{login:.+}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getAll() throws BackendException {
        log.debug("REST request to get all Transactions");
        return transactionRepository.findAll();
    }

    @Timed
    @RequestMapping(value = PEDELEC_ID_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getByPedelecId(@PathVariable Long pedelecId, @RequestParam(required = false) Integer resultSize) throws BackendException {
        log.debug("REST request to get last {} transactions for pedelec with pedelecId {}", resultSize, pedelecId);
        return transactionRepository.findByPedelecId(pedelecId, resultSize);
    }

    @Timed
    @RequestMapping(value = CUSTOMER_LOGIN_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getByLogin(@PathVariable String login, @RequestParam(required = false) Integer resultSize) throws BackendException {
        log.debug("REST request to get last {} transactions for user with login {}", resultSize, login);
        return transactionRepository.findByLogin(login, resultSize);
    }

    @Timed
    @RequestMapping(value = BASE_PATH_OPEN, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getOpen() throws BackendException {
        log.debug("REST request to get open Transactions");
        return transactionRepository.findOpen();
    }

    @Timed
    @RequestMapping(value = BASE_PATH_CLOSED, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getClosed() throws BackendException {
        log.debug("REST request to get closed Transactions");
        return transactionRepository.findClosed();
    }

    ///// Methods to catch exceptions /////

    @ExceptionHandler(BackendException.class)
    public void backendConflict(HttpServletResponse response, BackendException e) throws IOException {
        log.error("Exception happened", e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void conflict(HttpServletResponse response, Exception e) {
        log.error("Exception happened", e);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

}
