package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Transaction;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Transaction entity.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
