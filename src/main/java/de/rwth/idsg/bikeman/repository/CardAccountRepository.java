package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.CardAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by swam on 16/10/14.
 */
public interface CardAccountRepository extends JpaRepository<CardAccount, Long> {

    CardAccount findByCardIdAndCardPin(String cardId, String cardPin);
}
