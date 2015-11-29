package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.Pedelec;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("PedelecRepositoryImplApp")
@Slf4j
public class PedelecRepositoryImpl implements PedelecRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Pedelec> findAvailablePedelecs(Long stationId) throws AppException {
        final String q = "select p " +
            "from Pedelec p " +
            "join p.chargingStatus cs " +
            "join p.stationSlot sl " +
            "join sl.station s " +
            "where s.stationId = :stationId " +
            "and sl.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
            "and p.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
            "and p not in (select r.pedelec from Reservation r " +
            "where r.state = de.rwth.idsg.bikeman.domain.ReservationState.CREATED " +
            "and (:now between r.startDateTime and r.endDateTime)) " +
            "order by cs.batteryStateOfCharge desc";

        try {
            return em.createQuery(q, Pedelec.class)
                    .setParameter("stationId", stationId)
                    .setParameter("now", new LocalDateTime())
                    .setMaxResults(5)
                    .getResultList();
        } catch (Exception e) {
            throw new AppException("Failed to find pedelecs in station with ID " + stationId, e, AppErrorCode.DATABASE_OPERATION_FAILED);
        }
    }
}
