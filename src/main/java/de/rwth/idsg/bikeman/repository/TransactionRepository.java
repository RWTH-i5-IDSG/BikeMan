package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewTransactionDTO;

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

    List<ViewTransactionDTO> findAllCustomerTransactions() throws DatabaseException;

    List<ViewTransactionDTO> findAllMajorCustomerTransactions() throws DatabaseException;

    /*
    * Find OPEN (ONGOING) transactions.
    */
    List<ViewTransactionDTO> findOpen() throws DatabaseException;

    List<ViewTransactionDTO> findOpenCustomerTransactions() throws DatabaseException;

    List<ViewTransactionDTO> findOpenMajorCustomerTransactions() throws DatabaseException;

    /*
    * Find CLOSED transactions.
    */
    List<ViewTransactionDTO> findClosed() throws DatabaseException;

    List<ViewTransactionDTO> findClosedCustomerTransactions() throws DatabaseException;

    List<ViewTransactionDTO> findClosedMajorCustomerTransactions() throws DatabaseException;

    /*
    * Find OPEN and CLOSED transactions for ONE pedelec.
    *
    * @param resultSize   Limits the max number or results that are returned.
    *
    */
    List<ViewTransactionDTO> findByPedelecId(Long pedelecId, Integer resultSize) throws DatabaseException;

    List<ViewTransactionDTO> findCustomerTransactionsByPedelecId(Long pedelecId, Integer resultSize) throws DatabaseException;

    List<ViewTransactionDTO> findMajorCustomerTransactionsByPedelecId(Long pedelecId, Integer resultSize) throws DatabaseException;

    /*
    * Find OPEN and CLOSED transactions for ONE user.
    *
    * @param resultSize   Limits the max number or results that are returned.
    *
    */
    List<ViewTransactionDTO> findByLogin(String login, Integer resultSize) throws DatabaseException;

    List<ViewTransactionDTO> findCustomerTransactionsByLogin(String login, Integer resultSize) throws DatabaseException;

    List<ViewTransactionDTO> findMajorCustomerTransactionsByLogin(String login, Integer resultSize) throws DatabaseException;

    /*
    * Find the OPEN (ONGOING) transaction for ONE pedelec.
    *
    * Important: This is for internal use to close a transaction, and NOT for the Web UI
    */
    Transaction findOpenByPedelecId(Long pedelecId);

//    Transaction findOpenCustomerTransactionsByPedelecId(Long pedelecId);
//
//    Transaction findOpenMajorCustomerTransactionsByPedelecId(Long pedelecId);

    /*
    *
    * Important: This is for internal use to start a transaction, and NOT for the Web UI
    */
    Transaction start(StartTransactionDTO dto) throws DatabaseException;

    /*
    *
    * Important: This is for internal use to stop a transaction, and NOT for the Web UI
    */
    Transaction stop(StopTransactionDTO dto) throws DatabaseException;


}