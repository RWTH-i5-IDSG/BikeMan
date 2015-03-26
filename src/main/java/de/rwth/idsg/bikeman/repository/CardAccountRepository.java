package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by swam on 16/10/14.
 */
public interface CardAccountRepository extends JpaRepository<CardAccount, Long> {

    CardAccount findByCardIdAndCardPin(String cardId, String cardPin);

    List<CardAccount> findByUser(User user);

    CardAccount findByCardId(String cardId);

    @Query("select c from CardAccount c where UPPER(c.user.login) = UPPER(?1)")
    List<CardAccount> findByUserLogin(String login);


    @Query("select c from CardAccount c where c.activationKey = ?1")
    CardAccount findByActivationKey(String activationKey);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update CardAccount c set c.operationState = ?1 where c.cardId = ?2")
    int setOperationStateForCardId(OperationState operationState, String cardId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update CardAccount c set c.authenticationTrialCount = 0 where c.cardId = ?1")
    int resetAuthenticationTrialCount(String cardId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update CardAccount c set c.authenticationTrialCount = c.authenticationTrialCount + 1 where c.cardId = ?1")
    int incrementAuthenticationTrialCount(String cardId);

}
