package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Reservation;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by max on 24/11/14.
 */
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    @Query("SELECT r FROM Reservation r WHERE r.pedelec.pedelecId = :pedelecId AND (r.startDateTime <= :endTime AND :startTime <= r.endDateTime)")
    public List<Reservation> findByTimeFrameForPedelec(@Param("pedelecId") long pedelecId,
                                                       @Param("startTime") LocalDateTime start,
                                                       @Param("endTime") LocalDateTime end);

}
