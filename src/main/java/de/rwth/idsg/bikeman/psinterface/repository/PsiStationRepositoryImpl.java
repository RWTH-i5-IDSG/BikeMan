package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.ItemIdComparator;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.SlotDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StationStatusDTO;
import de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.06.2015
 */
@Repository
@Slf4j
public class PsiStationRepositoryImpl implements PsiStationRepository {

    @PersistenceContext private EntityManager em;
    @Inject private PedelecRepository pedelecRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAfterBoot(BootNotificationDTO dto, String endpointAddress) {

        // -------------------------------------------------------------------------
        // Find the station and update
        // -------------------------------------------------------------------------

        Station station;
        String stationManufacturerId = dto.getStationManufacturerId();
        try {
            station = findOneByManufacturerId(stationManufacturerId);
            station.setFirmwareVersion(dto.getFirmwareVersion());
            station.setEndpointAddress(endpointAddress);
            em.merge(station);

        } catch (NoResultException e) {
            throw new PsException("Station with manufacturerId '" + stationManufacturerId + "' is not registered", e,
                    PsErrorCode.NOT_REGISTERED);
        }

        // -------------------------------------------------------------------------
        // Find the slots, and decide whether to Update/Insert/Delete
        // -------------------------------------------------------------------------

        List<String> newList = new ArrayList<>();
        List<SlotDTO.Boot> stationSlotList = dto.getSlots();

        for (SlotDTO.Boot slot : stationSlotList) {
            newList.add(slot.getSlotManufacturerId());
        }

        final String q = "SELECT ss.manufacturerId FROM StationSlot ss WHERE ss.station = :station";

        List<String> dbList = em.createQuery(q, String.class)
                                .setParameter("station", station)
                                .getResultList();

        ItemIdComparator<String> idComparator = new ItemIdComparator<>();
        idComparator.setDatabaseList(dbList);
        idComparator.setNewList(newList);

        List<String> updateList = idComparator.getForUpdate();
        List<String> insertList = idComparator.getForInsert();
        List<String> deleteList = idComparator.getForDelete();

        // -------------------------------------------------------------------------
        // Update/Insert
        // -------------------------------------------------------------------------

        final String updateQuery = "UPDATE StationSlot ss " +
                                   "SET ss.isOccupied = :isOccupied, " +
                                   "ss.stationSlotPosition = :slotPosition, " +
                                   "ss.pedelec = (SELECT p FROM Pedelec p WHERE p.manufacturerId = :pedelecManufacturerId) " +
                                   "WHERE ss.station = :station " +
                                   "AND ss.manufacturerId = :slotManufacturerId";

        for (SlotDTO.Boot slot : stationSlotList) {
            String manuId = slot.getSlotManufacturerId();
            boolean hasPedelec = slot.getPedelecManufacturerId() != null;

            if (updateList.contains(manuId)) {
                em.createQuery(updateQuery)
                  .setParameter("isOccupied", hasPedelec)
                  .setParameter("slotPosition", slot.getSlotPosition())
                  .setParameter("pedelecManufacturerId", slot.getPedelecManufacturerId())
                  .setParameter("station", station)
                  .setParameter("slotManufacturerId", manuId)
                  .executeUpdate();

            } else if (insertList.contains(manuId)) {
                StationSlot newSlot = new StationSlot();
                newSlot.setManufacturerId(manuId);
                newSlot.setStationSlotPosition(slot.getSlotPosition());
                newSlot.setStation(station);
                newSlot.setIsOccupied(hasPedelec);

                if (hasPedelec) {
                    try {
                        Pedelec pedelec = pedelecRepository.findByManufacturerId(slot.getPedelecManufacturerId());
                        newSlot.setPedelec(pedelec);
                    } catch (DatabaseException e) {
                        throw new PsException(e.getMessage(), e, PsErrorCode.NOT_REGISTERED);
                    }
                }

                em.persist(newSlot);
            }
        }

        // -------------------------------------------------------------------------
        // Delete
        // -------------------------------------------------------------------------

        if (!deleteList.isEmpty()) {
            final String deleteQuery = "UPDATE StationSlot ss " +
                                       "SET ss.state = de.rwth.idsg.bikeman.domain.OperationState.DELETED " +
                                       "WHERE ss.manufacturerId IN :slotManufacturerIdList";

            em.createQuery(deleteQuery)
              .setParameter("slotManufacturerIdList", deleteList)
              .executeUpdate();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStationStatus(StationStatusDTO dto) {

        // -------------------------------------------------------------------------
        // Update Station
        // -------------------------------------------------------------------------

        final String s = "UPDATE Station s SET " +
                         "s.errorCode = :stationErrorCode, " +
                         "s.errorInfo = :stationErrorInfo, " +
                         "s.state = :stationState, " +
                         "s.updated = :updated " +
                         "WHERE s.manufacturerId = :stationManufacturerId";

        try {
            em.createQuery(s)
              .setParameter("stationErrorCode", dto.getStationErrorCode())
              .setParameter("stationErrorInfo", dto.getStationErrorInfo())
              .setParameter("stationState", OperationState.valueOf(dto.getStationState().name()))
              .setParameter("updated", new Date(Utils.toMillis(dto.getTimestamp())))
              .setParameter("stationManufacturerId", dto.getStationManufacturerId())
              .executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException("Failed to update the station status with manufacturerId "
                + dto.getStationManufacturerId(), e);
        }

        // -------------------------------------------------------------------------
        // Update Slots
        // -------------------------------------------------------------------------

        final String ss = "UPDATE StationSlot ss SET " +
                          "ss.errorCode = :slotErrorCode, " +
                          "ss.errorInfo = :slotErrorInfo, " +
                          "ss.state = :slotState " +
                          "WHERE ss.manufacturerId = :slotManufacturerId";

        for (SlotDTO.StationStatus slot : dto.getSlots()) {
            try {
                em.createQuery(ss)
                  .setParameter("slotErrorCode", slot.getSlotErrorCode())
                  .setParameter("slotErrorInfo", slot.getSlotErrorInfo())
                  .setParameter("slotState", OperationState.valueOf(slot.getSlotState().name()))
                  .setParameter("slotManufacturerId", slot.getSlotManufacturerId())
                  .executeUpdate();
            } catch (Exception e) {
                throw new DatabaseException("Failed to update the slot status with manufacturerId "
                    + slot.getSlotManufacturerId(), e);
            }
        }
    }

    @Transactional(readOnly = true)
    private Station findOneByManufacturerId(String manufacturerId) {
        return em.createQuery("SELECT s FROM Station s where s.manufacturerId = :stationManufacturerId", Station.class)
                 .setParameter("stationManufacturerId", manufacturerId)
                 .getSingleResult();
    }

}
