package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.web.rest.dto.view.ViewTransactionDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

import java.util.List;

public interface TransactionRepository {

    /*
    * Find ALL transactions.
    *
    */
    List<ViewTransactionDTO> findAllCustomerTransactions() throws DatabaseException;
    List<ViewTransactionDTO> findAllMajorCustomerTransactions() throws DatabaseException;

    /*
    * Find OPEN (ONGOING) transactions.
    */

    List<ViewTransactionDTO> findOpenCustomerTransactions() throws DatabaseException;
    List<ViewTransactionDTO> findOpenMajorCustomerTransactions() throws DatabaseException;

    /*
    * Find CLOSED transactions.
    */

    List<ViewTransactionDTO> findClosedCustomerTransactions() throws DatabaseException;
    List<ViewTransactionDTO> findClosedMajorCustomerTransactions() throws DatabaseException;

    /*
    * Find OPEN and CLOSED transactions for ONE pedelec.
    *
    * @param resultSize   Limits the max number or results that are returned.
    *
    */
    List<ViewTransactionDTO> findTransactionsByPedelecId(Long pedelecId, Integer resultSize) throws DatabaseException;

    /*
    * Find OPEN and CLOSED transactions for ONE user.
    *
    * @param resultSize   Limits the max number or results that are returned.
    *
    */

    List<ViewTransactionDTO> findCustomerTransactionsByLogin(String login, Integer resultSize) throws DatabaseException;
    List<ViewTransactionDTO> findMajorCustomerTransactionsByLogin(String login, Integer resultSize) throws DatabaseException;

    void kill(long transactionId);

}
