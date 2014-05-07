package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Pedelec;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Pedelec entity.
 */
public interface PedelecRepository extends JpaRepository<Pedelec, Long> {

}
