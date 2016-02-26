package de.rwth.idsg.bikeman.psinterface.repository;

import com.google.common.base.Strings;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.SlotDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StationStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.CardReadKeyDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.CardWriteKeyDTO;
import de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.utils.ItemIdComparator;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<CardReadKeyDTO> getCardReadKeys() {
        final String q = "SELECT new de.rwth.idsg.bikeman.psinterface.dto.response." +
                         "CardReadKeyDTO(c.name, c.readKey) " +
                         "FROM CardKey c";

        return em.createQuery(q, CardReadKeyDTO.class).getResultList();
    }

    @Override
    public CardWriteKeyDTO getCardWriteKey() {

        // TODO: the decision with "is not null" is dirty. ideally,
        // we should know beforehand what kind of card this is and get keys by the name
        final String q = "SELECT new de.rwth.idsg.bikeman.psinterface.dto.response." +
                         "CardWriteKeyDTO(c.name, c.readKey, c.writeKey, c.applicationKey, c.initialApplicationKey) " +
                         "FROM CardKey c WHERE c.initialApplicationKey is not null";

        return em.createQuery(q, CardWriteKeyDTO.class).getSingleResult();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAfterBoot(BootNotificationDTO dto) {

        // -------------------------------------------------------------------------
        // Find the station and update
        // -------------------------------------------------------------------------

        Station station;
        String stationManufacturerId = dto.getStationManufacturerId();
        try {
            station = findOneByManufacturerId(stationManufacturerId);
            station.setFirmwareVersion(dto.getFirmwareVersion());
            station.setEndpointAddress(dto.getStationURL());
            em.merge(station);

        } catch (NoResultException e) {
            throw new PsException("Station with manufacturerId '" + stationManufacturerId + "' is not registered", e,
                    PsErrorCode.NOT_REGISTERED);
        }

        // -------------------------------------------------------------------------
        // Find the slots, and decide whether to Update/Insert/Delete
        // -------------------------------------------------------------------------

        List<SlotDTO.Boot> stationSlotList = dto.getSlots();

        List<String> newList = stationSlotList.parallelStream()
                                              .map(SlotDTO.Boot::getSlotManufacturerId)
                                              .collect(Collectors.toList());

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
                                   "ss.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE, " +
                                   "ss.pedelec = (SELECT p FROM Pedelec p WHERE p.manufacturerId = :pedelecManufacturerId) " +
                                   "WHERE ss.station = :station " +
                                   "AND ss.manufacturerId = :slotManufacturerId";

        for (SlotDTO.Boot slot : stationSlotList) {
            String slotManufacturerId = slot.getSlotManufacturerId();

            String pedelecManufacturerId;
            if (Strings.isNullOrEmpty(slot.getPedelecManufacturerId())) {
                pedelecManufacturerId = null;
            } else {
                pedelecManufacturerId = slot.getPedelecManufacturerId();
            }

            boolean hasPedelec = (pedelecManufacturerId != null);

            if (updateList.contains(slotManufacturerId)) {
                em.createQuery(updateQuery)
                  .setParameter("isOccupied", hasPedelec)
                  .setParameter("slotPosition", slot.getSlotPosition())
                  .setParameter("pedelecManufacturerId", pedelecManufacturerId)
                  .setParameter("station", station)
                  .setParameter("slotManufacturerId", slotManufacturerId)
                  .executeUpdate();

            } else if (insertList.contains(slotManufacturerId)) {
                StationSlot newSlot = new StationSlot();
                newSlot.setManufacturerId(slotManufacturerId);
                newSlot.setStationSlotPosition(slot.getSlotPosition());
                newSlot.setStation(station);
                newSlot.setIsOccupied(hasPedelec);
                newSlot.setState(OperationState.OPERATIVE);

                if (hasPedelec) {
                    try {
                        Pedelec pedelec = pedelecRepository.findByManufacturerId(pedelecManufacturerId);
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
                                       "WHERE ss.manufacturerId IN :slotManufacturerIdList " +
                                       "AND ss.station = :station";

            em.createQuery(deleteQuery)
              .setParameter("station", station)
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
              .setParameter("updated", new Date(dto.getTimestamp().getMillis()))
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
                          "WHERE ss.manufacturerId = :slotManufacturerId " +
                          "AND ss.station = (SELECT s FROM Station s WHERE s.manufacturerId = :stationManufacturerId)";

        for (SlotDTO.StationStatus slot : dto.getSlots()) {
            try {
                em.createQuery(ss)
                  .setParameter("slotErrorCode", slot.getSlotErrorCode())
                  .setParameter("slotErrorInfo", slot.getSlotErrorInfo())
                  .setParameter("slotState", OperationState.valueOf(slot.getSlotState().name()))
                  .setParameter("slotManufacturerId", slot.getSlotManufacturerId())
                  .setParameter("stationManufacturerId", dto.getStationManufacturerId())
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
