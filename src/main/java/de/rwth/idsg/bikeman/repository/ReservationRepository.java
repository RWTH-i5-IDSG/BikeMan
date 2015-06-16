package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Reservation;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by max on 24/11/14.
 */
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    @Query("SELECT r FROM Reservation r " +
           "WHERE r.pedelec.pedelecId = :pedelecId " +
           "AND (r.startDateTime <= :endTime " +
           "AND :startTime <= r.endDateTime)")
    List<Reservation> findByTimeFrameForPedelec(@Param("pedelecId") long pedelecId,
                                                @Param("startTime") LocalDateTime start,
                                                @Param("endTime") LocalDateTime end);

    @Query("SELECT r FROM Reservation r " +
        "WHERE r.pedelec.pedelecId = :pedelecId " +
        "AND NOT (r.reservationId = :reservationId)" +
        "AND (r.startDateTime <= :endTime " +
        "AND :startTime <= r.endDateTime)")
    List<Reservation> findOverlappingReservations(@Param("pedelecId") long pedelecId,
                                                  @Param("reservationId") long reservationId,
                                                  @Param("startTime") LocalDateTime start,
                                                  @Param("endTime") LocalDateTime end);

    @Modifying
    @Query("UPDATE Reservation r " +
           "SET r.startDateTime = :begin, r.endDateTime = :end " +
           "WHERE r.reservationId = :reservationId")
    void updateTimeWindow(@Param("reservationId") long reservationId,
                                 @Param("begin") LocalDateTime begin,
                                 @Param("end") LocalDateTime end);

    @Query("SELECT r FROM Reservation r " +
           "WHERE r.cardAccount.cardAccountId = :cardAccountID " +
           "AND (r.startDateTime <= :dateTime AND r.endDateTime >= :dateTime)")
    Reservation findByCustomerIdAndTime (@Param("cardAccountID") long cardAccountId,
                                         @Param("dateTime") LocalDateTime dateTime);

    @Modifying
    @Query("UPDATE Reservation r SET r.endDateTime = :dateTime WHERE r.reservationId = :reservationId")
    void updateEndDateTime(@Param("reservationId") long reservationId,
                           @Param("dateTime") LocalDateTime dateTime);
}
