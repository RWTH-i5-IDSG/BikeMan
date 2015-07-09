package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
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

    @Inject private CardAccountRepository cardAccountRepository;

    @Override
    @Transactional(readOnly = true)
    public CardAccount findByCardId(String cardId) throws DatabaseException {
        final String query = "SELECT c FROM CardAccount c WHERE c.cardId = :cardId";
        try {
            return em.createQuery(query, CardAccount.class)
                .setParameter("cardId", cardId)
                .getSingleResult();

        } catch (NoResultException e) {
            throw new PsException("No customer found with cardId " + cardId,
                e, PsErrorCode.NOT_REGISTERED);

        } catch (Exception e) {
            throw new PsException("Failed during database operation.", e, PsErrorCode.DATABASE_OPERATION_FAILED);
        }
    }

    @Override
    @Transactional
    public CardAccount saveCardAccount(CardAccount cardAccount) {
        cardAccountRepository.save(cardAccount);

        return cardAccount;
    }

    @Override
    @Transactional
    public void resetAuthenticationTrialCount(CardAccount cardAccount) {
        cardAccount.setAuthenticationTrialCount(0);
        cardAccountRepository.save(cardAccount);
    }
}
