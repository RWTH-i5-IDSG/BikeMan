package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.06.2015
 */
public interface PsiTransactionRepository {

    /**
     * Count the number of OPEN (ONGOING) transactions for ONE card account.
     *
     */
    int countOpenTransactions(String cardId);

    Transaction start(StartTransactionDTO dto);
    Transaction stop(StopTransactionDTO dto);
}
