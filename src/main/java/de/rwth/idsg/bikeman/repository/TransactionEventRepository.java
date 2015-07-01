package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.TransactionEvent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Wolfgang Kluth on 01/07/15.
 */
public interface TransactionEventRepository extends JpaRepository<TransactionEvent, Long> {

}
