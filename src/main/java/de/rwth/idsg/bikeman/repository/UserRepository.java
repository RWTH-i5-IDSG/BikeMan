package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    Optional<User> findOneByLogin(String login);

    User findByLoginIgnoreCase(String login);

}
