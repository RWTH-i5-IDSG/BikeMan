package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.domain.Reservation;
import de.rwth.idsg.bikeman.domain.ReservationState;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.06.2015
 */
public interface PsiReservationRepository extends JpaRepository<Reservation, String> {

    @Query("SELECT r FROM Reservation r " +
           "WHERE r.cardAccount.cardAccountId = :cardAccountId " +
           "AND r.pedelec.pedelecId = :pedelecId " +
           "AND r.startDateTime <= :startTime " +
           "AND :startTime <= r.endDateTime " +
           "AND r.state = de.rwth.idsg.bikeman.domain.ReservationState.CREATED")
    List<Reservation> find(@Param("cardAccountId") long cardAccountId,
                           @Param("pedelecId") long pedelecId,
                           @Param("startTime") LocalDateTime startTime);

    @Modifying
    @Query("UPDATE Reservation r SET r.state = :state WHERE r = :reservation")
    void updateState(@Param("reservation") Reservation reservation,
                     @Param("state") ReservationState state);
}
