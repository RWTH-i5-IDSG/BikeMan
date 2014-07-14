package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Transaction;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewTransactionDTO;

import java.util.List;

/**
 * Spring Data JPA repository for the Transaction entity.
 */
public interface TransactionRepository {
//
//    @Query("select t from Transaction t where t.pedelec = :pedelec and t.toSlot = null")
//    Transaction findLastTransactionByPedelecId(@Param("pedelec") Pedelec pedelec);

    /*
    * Find ALL transactions.
    */
    List<ViewTransactionDTO> findAll() throws DatabaseException;

    /*
    * Find OPEN (ONGOING) transactions.
    */
    List<ViewTransactionDTO> findOpen() throws DatabaseException;

    /*
    * Find CLOSED transactions.
    */
    List<ViewTransactionDTO> findClosed() throws DatabaseException;

    /*
    * Find OPEN and CLOSED transactions for ONE pedelec.
    *
    * @param resultSize   Limits the max number or results that are returned.
    *
    */
    List<ViewTransactionDTO> findByPedelecId(Long pedelecId, Integer resultSize) throws DatabaseException;

    /*
    * Find OPEN and CLOSED transactions for ONE user.
    *
    * @param resultSize   Limits the max number or results that are returned.
    *
    */
    List<ViewTransactionDTO> findByLogin(String login, Integer resultSize) throws DatabaseException;

    /*
    * Find the OPEN (ONGOING) transaction for ONE pedelec.
    *
    * Important: This is for internal use to close a transaction, and NOT for the Web UI
    */
    Transaction findOpenByPedelecId(Long pedelecId);

    /*
    *
    * Important: This is for internal use to start a transaction, and NOT for the Web UI
    */
    void start(Transaction transaction);

    /*
    *
    * Important: This is for internal use to stop a transaction, and NOT for the Web UI
    */
    void stop(Transaction transaction);


}