package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.login.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    User findByLoginIgnoreCase(String login);

}
