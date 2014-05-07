package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Address;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Address entity.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

}
