package de.rwth.idsg.velocity.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.velocity.domain.Transaction;
import de.rwth.idsg.velocity.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Transaction.
 */
@RestController
@RequestMapping("/app")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    @Inject
    private TransactionRepository transactionRepository;

    /**
     * POST  /rest/transactions -> Create a new transaction.
     */
    @RequestMapping(value = "/rest/transactions",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void create(@RequestBody Transaction transaction) {
        log.debug("REST request to save Transaction : {}", transaction);
        transactionRepository.save(transaction);
    }

    /**
     * GET  /rest/transactions -> get all the transactions.
     */
    @RequestMapping(value = "/rest/transactions",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<Transaction> getAll() {
        log.debug("REST request to get all Transactions");
        return transactionRepository.findAll();
    }

    /**
     * GET  /rest/transactions/:id -> get the "id" transaction.
     */
    @RequestMapping(value = "/rest/transactions/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public Transaction get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Transaction : {}", id);
        Transaction transaction = transactionRepository.findOne(id);
        if (transaction == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return transaction;
    }

    /**
     * DELETE  /rest/transactions/:id -> delete the "id" transaction.
     */
    @RequestMapping(value = "/rest/transactions/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to delete Transaction : {}", id);
        transactionRepository.delete(id);
    }
}
