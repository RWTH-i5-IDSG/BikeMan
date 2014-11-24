package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by max on 24/11/14.
 */
public interface ReservationRepository extends JpaRepository<Reservation, String> {
}
