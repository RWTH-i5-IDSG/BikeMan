package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.06.2015
 */
public interface PsiCustomerRepository {
    /**
     * Find unique customer with card-id and pin
     *
     * @return userId
     */
    CardAccount findByCardIdAndCardPin(String cardId, String cardPin) throws DatabaseException;
}
