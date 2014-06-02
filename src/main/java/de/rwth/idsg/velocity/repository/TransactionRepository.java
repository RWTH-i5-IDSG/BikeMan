package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Transaction;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditPedelecDTO;
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
    List<ViewTransactionDTO> findAll();

    /*
    * Find OPEN (ONGOING) transactions.
    */
    List<ViewTransactionDTO> findOpen();

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