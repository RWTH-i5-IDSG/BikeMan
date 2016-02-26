package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Address entity.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

}
