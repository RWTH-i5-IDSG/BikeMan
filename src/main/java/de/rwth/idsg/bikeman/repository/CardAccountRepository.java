package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.login.User;
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

    @Query("select c from CardAccount c where UPPER(c.user.login) = UPPER(?1)")
    List<CardAccount> findByUserLogin(String login);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update CardAccount c set c.operationState = ?1 where c.cardId = ?2")
    int setOperationStateForCardId(OperationState operationState, String cardId);
}
