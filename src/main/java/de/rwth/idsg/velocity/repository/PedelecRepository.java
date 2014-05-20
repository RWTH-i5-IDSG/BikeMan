package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.web.rest.dto.ViewPedelecDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedelec entity.
 */
public interface PedelecRepository extends JpaRepository<Pedelec, Long> {

    // TODO: Resolve the problem with getting stationSlot, and provide it to the front end
    @Query("select new de.rwth.idsg.velocity.web.rest.dto.ViewPedelecDTO(ped.pedelecId, ped.stateOfCharge, ped.state) from Pedelec ped")
    List<ViewPedelecDTO> viewPedelecs();

}
