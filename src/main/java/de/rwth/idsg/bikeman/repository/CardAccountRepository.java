package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.OperationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by swam on 16/10/14.
 */
public interface CardAccountRepository extends JpaRepository<CardAccount, Long> {

    CardAccount findByCardIdAndCardPin(String cardId, String cardPin);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update CardAccount c set c.operationState = ?1 where c.cardAccountId = ?2")
    int setOperationStateFor(OperationState operationState, Long cardAccountId);
}
