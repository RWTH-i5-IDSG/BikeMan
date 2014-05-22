package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.login.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, String> {

}
