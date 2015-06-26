package de.rwth.idsg.bikeman.web.rest;

import de.rwth.idsg.bikeman.repository.TransactionRepository;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewTransactionDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing Transaction.
 */
@RestController
@RequestMapping(value = "/api", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TransactionResource {

    @Inject private TransactionRepository transactionRepository;

    private static final String BASE_PATH = "/transactions";
    private static final String BASE_PATH_OPEN = "/transactions/open";
    private static final String BASE_PATH_CLOSED = "/transactions/closed";
    private static final String PEDELEC_ID_PATH = "/transactions/pedelec/{pedelecId}";
    private static final String CUSTOMER_LOGIN_PATH = "/transactions/customer/{login:.+}";

    private static final String MAJOR_CUSTOMER_PATH = "/major-customer/transactions";
    private static final String MAJOR_CUSTOMER_PATH_OPEN = "/major-customer/transactions/open";
    private static final String MAJOR_CUSTOMER_PATH_CLOSED = "/major-customer/transactions/closed";
    private static final String MAJOR_CUSTOMER_PEDELEC_ID_PATH = "/major-customer/transactions/pedelec/{pedelecId}";
    private static final String MAJOR_CUSTOMER_LOGIN_PATH = "/major-customer/transactions/customer/{login:.+}";

    private static final String KILL_PATH = "/transactions/kill/{transactionId}";

    @RequestMapping(value = BASE_PATH)
    public List<ViewTransactionDTO> getAll() throws DatabaseException {
        log.debug("REST request to get all Customer Transactions");
        return transactionRepository.findAllCustomerTransactions();
    }

    @RequestMapping(value = MAJOR_CUSTOMER_PATH)
    public List<ViewTransactionDTO> getAllMajorCustomerTransactions() throws DatabaseException {
        log.debug("REST request to get all Major Customer Transactions");
        return transactionRepository.findAllMajorCustomerTransactions();
    }

    @RequestMapping(value = PEDELEC_ID_PATH)
    public List<ViewTransactionDTO> getByPedelecId(
        @PathVariable Long pedelecId, @RequestParam(required = false) Integer resultSize)
        throws DatabaseException {

        log.debug("REST request to get last {} customer transactions for pedelec with pedelecId {}", resultSize, pedelecId);
        return transactionRepository.findCustomerTransactionsByPedelecId(pedelecId, resultSize);
    }

    @RequestMapping(value = MAJOR_CUSTOMER_PEDELEC_ID_PATH)
    public List<ViewTransactionDTO> getMajorCustomerTransactionsByPedelecId(
        @PathVariable Long pedelecId, @RequestParam(required = false) Integer resultSize)
        throws DatabaseException {

        log.debug("REST request to get last {} major customer transactions for pedelec with pedelecId {}", resultSize, pedelecId);
        return transactionRepository.findMajorCustomerTransactionsByPedelecId(pedelecId, resultSize);
    }

    @RequestMapping(value = CUSTOMER_LOGIN_PATH)
    public List<ViewTransactionDTO> getByLogin(
        @PathVariable String login, @RequestParam(required = false) Integer resultSize)
        throws DatabaseException {

        log.debug("REST request to get last {} customer transactions for user with login {}", resultSize, login);
        return transactionRepository.findCustomerTransactionsByLogin(login, resultSize);
    }

    @RequestMapping(value = MAJOR_CUSTOMER_LOGIN_PATH)
    public List<ViewTransactionDTO> getMajorCustomerTransactionsByLogin(
        @PathVariable String login, @RequestParam(required = false) Integer resultSize)
        throws DatabaseException {

        log.debug("REST request to get last {} major customer transactions for user with login {}", resultSize, login);
        return transactionRepository.findMajorCustomerTransactionsByLogin(login, resultSize);
    }

    @RequestMapping(value = BASE_PATH_OPEN)
    public List<ViewTransactionDTO> getOpen() throws DatabaseException {
        log.debug("REST request to get open Customer Transactions");
        return transactionRepository.findOpenCustomerTransactions();
    }

    @RequestMapping(value = MAJOR_CUSTOMER_PATH_OPEN)
    public List<ViewTransactionDTO> getOpenMajorCustomerTransactions() throws DatabaseException {
        log.debug("REST request to get open Major Customer Transactions");
        return transactionRepository.findOpenMajorCustomerTransactions();
    }

    @RequestMapping(value = BASE_PATH_CLOSED)
    public List<ViewTransactionDTO> getClosed() throws DatabaseException {
        log.debug("REST request to get closed Customer Transactions");
        return transactionRepository.findClosedCustomerTransactions();
    }

    @RequestMapping(value = MAJOR_CUSTOMER_PATH_CLOSED)
    public List<ViewTransactionDTO> getClosedMajorCustomerTransactions() throws DatabaseException {
        log.debug("REST request to get closed Major Customer Transactions");
        return transactionRepository.findClosedMajorCustomerTransactions();
    }

    @RequestMapping(value = KILL_PATH)
    public void kill(@PathVariable Long transactionId) throws DatabaseException {
        transactionRepository.kill(transactionId);
    }
}
