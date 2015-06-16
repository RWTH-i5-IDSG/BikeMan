package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.06.2015
 */
@Repository
@Slf4j
public class PsiCustomerRepositoryImpl implements PsiCustomerRepository {

    @PersistenceContext private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public CardAccount findByCardIdAndCardPin(String cardId, String cardPin) throws DatabaseException {
        final String query = "SELECT c FROM CardAccount c WHERE c.cardId = :cardId AND c.cardPin = :cardPin";
        try {
            return em.createQuery(query, CardAccount.class)
                .setParameter("cardId", cardId)
                .setParameter("cardPin", cardPin)
                .getSingleResult();

        } catch (NoResultException e) {
            throw new PsException("No customer found with cardId " + cardId
                + " and cardPin " + cardPin, e, PsErrorCode.CONSTRAINT_FAILED);

        } catch (Exception e) {
            throw new PsException("Failed during database operation.", e, PsErrorCode.DATABASE_OPERATION_FAILED);
        }
    }
}
