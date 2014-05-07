package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.StationSlot;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the StationSlot entity.
 */
public interface StationSlotRepository extends JpaRepository<StationSlot, Long> {

}
