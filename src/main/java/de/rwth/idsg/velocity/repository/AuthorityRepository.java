package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.login.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
