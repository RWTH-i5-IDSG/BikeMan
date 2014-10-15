package de.rwth.idsg.bikeman.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.bikeman.repository.TransactionRepository;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewTransactionDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
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

    private static final String MAJOR_CUSTOMER_PATH = "/rest/major-customer/transactions";
    private static final String MAJOR_CUSTOMER_PATH_OPEN = "/rest/major-customer/transactions/open";
    private static final String MAJOR_CUSTOMER_PATH_CLOSED = "/rest/major-customer/transactions/closed";
    private static final String MAJOR_CUSTOMER_PEDELEC_ID_PATH = "/rest/major-customer/transactions/pedelec/{pedelecId}";
    private static final String MAJOR_CUSTOMER_LOGIN_PATH = "/rest/major-customer/transactions/customer/{login:.+}";

    @Timed
    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getAll() throws DatabaseException {
        log.debug("REST request to get all Customer Transactions");
        return transactionRepository.findAllCustomerTransactions();
    }

    @Timed
    @RequestMapping(value = MAJOR_CUSTOMER_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getAllMajorCustomerTransactions() throws DatabaseException {
        log.debug("REST request to get all Major Customer Transactions");
        return transactionRepository.findAllMajorCustomerTransactions();
    }

    @Timed
    @RequestMapping(value = PEDELEC_ID_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getByPedelecId(@PathVariable Long pedelecId, @RequestParam(required = false) Integer resultSize) throws DatabaseException {
        log.debug("REST request to get last {} customer transactions for pedelec with pedelecId {}", resultSize, pedelecId);
        return transactionRepository.findCustomerTransactionsByPedelecId(pedelecId, resultSize);
    }

    @Timed
    @RequestMapping(value = MAJOR_CUSTOMER_PEDELEC_ID_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getMajorCustomerTransactionsByPedelecId(@PathVariable Long pedelecId, @RequestParam(required = false) Integer resultSize) throws DatabaseException {
        log.debug("REST request to get last {} major customer transactions for pedelec with pedelecId {}", resultSize, pedelecId);
        return transactionRepository.findMajorCustomerTransactionsByPedelecId(pedelecId, resultSize);
    }

    @Timed
    @RequestMapping(value = CUSTOMER_LOGIN_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getByLogin(@PathVariable String login, @RequestParam(required = false) Integer resultSize) throws DatabaseException {
        log.debug("REST request to get last {} customer transactions for user with login {}", resultSize, login);
        return transactionRepository.findCustomerTransactionsByLogin(login, resultSize);
    }

    @Timed
    @RequestMapping(value = MAJOR_CUSTOMER_LOGIN_PATH, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getMajorCustomerTransactionsByLogin(@PathVariable String login, @RequestParam(required = false) Integer resultSize) throws DatabaseException {
        log.debug("REST request to get last {} major customer transactions for user with login {}", resultSize, login);
        return transactionRepository.findMajorCustomerTransactionsByLogin(login, resultSize);
    }

    @Timed
    @RequestMapping(value = BASE_PATH_OPEN, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getOpen() throws DatabaseException {
        log.debug("REST request to get open Customer Transactions");
        return transactionRepository.findOpenCustomerTransactions();
    }

    @Timed
    @RequestMapping(value = MAJOR_CUSTOMER_PATH_OPEN, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getOpenMajorCustomerTransactions() throws DatabaseException {
        log.debug("REST request to get open Major Customer Transactions");
        return transactionRepository.findOpenMajorCustomerTransactions();
    }

    @Timed
    @RequestMapping(value = BASE_PATH_CLOSED, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getClosed() throws DatabaseException {
        log.debug("REST request to get closed Customer Transactions");
        return transactionRepository.findClosedCustomerTransactions();
    }

    @Timed
    @RequestMapping(value = MAJOR_CUSTOMER_PATH_CLOSED, method = RequestMethod.GET)
    public List<ViewTransactionDTO> getClosedMajorCustomerTransactions() throws DatabaseException {
        log.debug("REST request to get closed Major Customer Transactions");
        return transactionRepository.findClosedMajorCustomerTransactions();
    }
}
