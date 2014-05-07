package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Customer;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Customer entity.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
