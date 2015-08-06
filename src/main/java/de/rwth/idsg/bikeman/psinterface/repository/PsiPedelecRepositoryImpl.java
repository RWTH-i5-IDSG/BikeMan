package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.ChargingStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.PedelecStatusDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.06.2015
 */
@Repository
@Slf4j
public class PsiPedelecRepositoryImpl implements PsiPedelecRepository {

    @PersistenceContext private EntityManager em;

    // nachfolgende funktion nur mit einem rückgabewert (keine liste)

    @Override
    @Transactional(readOnly = true)
    public List<String> findAvailablePedelecs(String stationManufacturerId) {
//        final String q = "select p.manufacturerId " +
//                         "from Pedelec p " +
//                         "join p.chargingStatus cs " +
//                         "join p.stationSlot ss " +
//                         "join ss.station s " +
//                         "left join p.reservations r on (p = r.pedelec " +
//                            "and not r.state = de.rwth.idsg.bikeman.domain.ReservationState.USED " +
//                            "and (CURRENT_TIMESTAMP between r.startDateTime and r.endDateTime)) " +
//                         "where s.manufacturerId = :stationManufacturerId " +
//                         "and ss.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
//                         "and p.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
//                         "and r.pedelec IS NULL " +
//                         "order by cs.batteryStateOfCharge desc";

        final String q = "select p.manufacturerId " +
                         "from Pedelec p " +
                         "join p.chargingStatus cs " +
                         "join p.stationSlot sl " +
                         "join sl.station s " +
                         "where s.manufacturerId = :stationManufacturerId " +
                         "and sl.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
                         "and p.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
                         "and p not in (select r.pedelec from Reservation r " +
                            "where r.state = de.rwth.idsg.bikeman.domain.ReservationState.CREATED " +
                            "and (:now between r.startDateTime and r.endDateTime)) " +
                         "order by cs.batteryStateOfCharge desc";

        try {
            return em.createQuery(q, String.class)
                     .setParameter("stationManufacturerId", stationManufacturerId)
                     .setParameter("now", new LocalDateTime())
                     .setMaxResults(5)
                     .getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to find pedelecs in station with manufacturerId "
                    + stationManufacturerId, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findReservedPedelecs(String stationManufacturerId, String cardId) {
        final String q = "SELECT p.manufacturerId " +
                         "FROM Reservation r " +
                         "JOIN r.pedelec p " +
                         "JOIN r.cardAccount ca " +
                         "JOIN p.stationSlot ss " +
                         "JOIN ss.station s " +
                         "WHERE ca.cardId = :cardId " +
                         "AND (:now BETWEEN r.startDateTime AND r.endDateTime) " +
                         "AND s.manufacturerId = :stationManufacturerId " +
                         "AND ss.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
                         "AND p.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
                         "AND r.state = de.rwth.idsg.bikeman.domain.ReservationState.CREATED";

        try {
            return em.createQuery(q, String.class)
                .setParameter("stationManufacturerId", stationManufacturerId)
                .setParameter("cardId", cardId)
                .setParameter("now", new LocalDateTime())
                .setMaxResults(1)
                .getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to find pedelecs in station with manufacturerId "
                + stationManufacturerId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePedelecStatus(PedelecStatusDTO dto) {
        final String s = "UPDATE Pedelec p SET " +
            "p.errorCode = :pedelecErrorCode, " +
            "p.errorInfo = :pedelecErrorInfo, " +
            "p.state = :pedelecState, " +
            "p.updated = :updated " +
            "WHERE p.manufacturerId = :pedelecManufacturerId";

        try {
            em.createQuery(s)
                .setParameter("pedelecErrorCode", dto.getPedelecErrorCode())
                .setParameter("pedelecErrorInfo", dto.getPedelecErrorInfo())
                .setParameter("pedelecState", OperationState.valueOf(dto.getPedelecState().name()))
                .setParameter("updated", new Date(Utils.toMillis(dto.getTimestamp())))
                .setParameter("pedelecManufacturerId", dto.getPedelecManufacturerId())
                .executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException("Failed to update the pedelec status with manufacturerId "
                + dto.getPedelecManufacturerId(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePedelecChargingStatus(List<ChargingStatusDTO> dtoList) {
        final String s = "UPDATE PedelecChargingStatus s SET " +
            "s.state = :state, " +
            "s.meterValue = :meterValue, " +
            "s.batteryCycleCount = :cycleCount, " +
            "s.batteryStateOfCharge = :stateOfCharge, " +
            "s.batteryTemperature = :temperature, " +
            "s.batteryVoltage = :voltage, " +
            "s.batteryCurrent = :current, " +
            "s.timestamp = :timestamp " +
            "WHERE s.pedelec = (SELECT p FROM Pedelec p WHERE p.manufacturerId = :pedelecManufacturerId)";

        try {
            for (ChargingStatusDTO dto : dtoList) {
                em.createQuery(s)
                    .setParameter("state", dto.getChargingState())
                    .setParameter("meterValue", dto.getMeterValue())
                    .setParameter("cycleCount", dto.getBattery().getCycleCount())
                    .setParameter("stateOfCharge", dto.getBattery().getSoc())
                    .setParameter("temperature", dto.getBattery().getTemperature())
                    .setParameter("voltage", dto.getBattery().getVoltage())
                    .setParameter("current", dto.getBattery().getCurrent())
                    .setParameter("timestamp", new LocalDateTime(Utils.toMillis(dto.getTimestamp())))
                    .setParameter("pedelecManufacturerId", dto.getPedelecManufacturerId())
                    .executeUpdate();
            }
        } catch (Exception e) {
            throw new DatabaseException("Failed to update the charging status.", e);
        }
    }

}
