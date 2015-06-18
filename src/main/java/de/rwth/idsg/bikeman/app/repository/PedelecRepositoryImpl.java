package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.Pedelec;
import lombok.extern.slf4j.Slf4j;
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
        final String q = "SELECT p " +
                "FROM Pedelec p " +
                "JOIN p.chargingStatus cs " +
                "WHERE p.stationSlot.station.stationId = :stationId " +
                "AND p.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
                "ORDER BY cs.batteryStateOfCharge DESC";

        try {
            return em.createQuery(q, Pedelec.class)
                    .setParameter("stationId", stationId)
                    .setMaxResults(5)
                    .getResultList();
        } catch (Exception e) {
            throw new AppException("Failed to find pedelecs in station with ID " + stationId, e, AppErrorCode.DATABASE_OPERATION_FAILED);
        }
    }
}
