package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Station;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Station entity.
 */
public interface StationRepository extends JpaRepository<Station, Long> {

}
