package de.rwth.idsg.bikeman.psinterface.repository;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.repository.helper.JpaHelper;
import de.rwth.idsg.bikeman.service.TariffService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.06.2015
 */
@Repository
@Slf4j
public class PsiTransactionRepositoryImpl implements PsiTransactionRepository {

    @PersistenceContext private EntityManager em;
    @Inject private TariffService tariffService;

    @Override
    @Transactional(readOnly = true)
    public boolean hasOpenTransactions(String cardId) {
        final String query = "SELECT COUNT(t) FROM Transaction t " +
                             "WHERE t.cardAccount.cardId = :cardId AND t.endDateTime IS NULL AND t.toSlot IS NULL";

        Long count = em.createQuery(query, Long.class)
                       .setParameter("cardId", cardId)
                       .getSingleResult();

        return count >= 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction start(StartTransactionDTO dto) throws DatabaseException {

        // -------------------------------------------------------------------------
        // 1. Start transaction
        // -------------------------------------------------------------------------

        final String pedelecQuery = "SELECT p FROM Pedelec p WHERE p.manufacturerId = :pedelecManufacturerId";

        final String cardAccQuery = "SELECT ca FROM CardAccount ca WHERE ca.cardId = :cardId";

        Pedelec pedelec = em.createQuery(pedelecQuery, Pedelec.class)
                            .setParameter("pedelecManufacturerId", dto.getPedelecManufacturerId())
                            .getSingleResult();

        CardAccount cardAccount = em.createQuery(cardAccQuery, CardAccount.class)
                                    .setParameter("cardId", dto.getCardId())
                                    .getSingleResult();

        StationSlot slot = pedelec.getStationSlot();

        // Check integrity of station slot
        String entitySlotManId = slot.getManufacturerId();
        String dtoSlotManId = dto.getSlotManufacturerId();
        boolean slotOK = entitySlotManId.equalsIgnoreCase(dtoSlotManId);
        if (!slotOK) {
            throw new PsException("Integrity check of station slot manufacturerId failed: "
                + dtoSlotManId  + " (sent from station) != "
                + entitySlotManId + " (DB value)", PsErrorCode.CONSTRAINT_FAILED);
        }

        // Check integrity of station
        String entityStationManId = slot.getStation().getManufacturerId();
        String dtoStationManId = dto.getStationManufacturerId();
        boolean stationOK = entityStationManId.equalsIgnoreCase(dtoStationManId);
        if (!stationOK) {
            throw new PsException("Integrity check of station manufacturerId failed: "
                + dtoStationManId + " (sent from station) != "
                + entityStationManId + " (DB value)", PsErrorCode.CONSTRAINT_FAILED);
        }

        Transaction transaction = new Transaction();

        // add one sec to timestamp, because of rounding error: UNIX timestamp kills millis
        Long timestampInMillis = Utils.toMillis(dto.getTimestamp() + 1L);

        transaction.setStartDateTime(new LocalDateTime(timestampInMillis));
        transaction.setCardAccount(cardAccount);
        transaction.setPedelec(pedelec);
        transaction.setFromSlot(slot);
        transaction.setBookedTariff(cardAccount.getCurrentTariff());
        em.persist(transaction);

        // -------------------------------------------------------------------------
        // 2. Update related entities
        // -------------------------------------------------------------------------

        cardAccount.setInTransaction(true);
        pedelec.setInTransaction(true);
        pedelec.setStationSlot(null);
        slot.setIsOccupied(false);
        slot.setPedelec(null);

        em.merge(cardAccount);
        em.merge(pedelec);
        em.merge(slot);

        return transaction;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction stop(StopTransactionDTO dto) throws DatabaseException {

        // -------------------------------------------------------------------------
        // 1. End transaction
        // -------------------------------------------------------------------------

        final String endQuery = "SELECT t FROM Transaction t " +
                                "INNER JOIN t.bookedTariff " +
                                "WHERE t.pedelec = (SELECT p FROM Pedelec p WHERE p.manufacturerId = :pedelecManufacturerId) " +
                                "AND t.toSlot IS NULL " +
                                "AND t.endDateTime IS NULL";

        final String slotQuery = "SELECT ss FROM StationSlot ss " +
                                 "WHERE ss.manufacturerId = :slotManufacturerId " +
                                 "AND ss.station = (SELECT s FROM Station s WHERE s.manufacturerId = :stationManufacturerId)";

        final String pedelecQuery = "SELECT p FROM Pedelec p WHERE p.manufacturerId = :pedelecManufacturerId";

        Transaction transaction = JpaHelper.getSingleResult(em.createQuery(endQuery, Transaction.class)
                                              .setParameter("pedelecManufacturerId", dto.getPedelecManufacturerId()));

        StationSlot slot = em.createQuery(slotQuery, StationSlot.class)
                             .setParameter("slotManufacturerId", dto.getSlotManufacturerId())
                             .setParameter("stationManufacturerId", dto.getStationManufacturerId())
                             .getSingleResult();

        Pedelec pedelec = em.createQuery(pedelecQuery, Pedelec.class)
                            .setParameter("pedelecManufacturerId", dto.getPedelecManufacturerId()).getSingleResult();

        if (transaction == null) {
            log.error("StopTransaction is missing StartTransaction. Skip Transaction and rearrange pedelec.");

            em.createQuery("UPDATE StationSlot ss SET ss.pedelec = NULL, ss.isOccupied = FALSE WHERE ss.pedelec = :pedelec")
              .setParameter("pedelec", pedelec)
              .executeUpdate();

            // Register the pedelec back at the starting station slot
            em.createQuery("UPDATE StationSlot ss SET ss.isOccupied = true, ss.pedelec = :pedelec WHERE ss = :stationSlot")
              .setParameter("pedelec", pedelec)
              .setParameter("stationSlot", slot)
              .executeUpdate();

            return null;
        }

        Long timestampInMillis = Utils.toMillis(dto.getTimestamp());
        transaction.setEndDateTime(new LocalDateTime(timestampInMillis));
        transaction.setToSlot(slot);
        transaction.setFees(tariffService.calculatePrice(transaction));
        Transaction mergedTransaction = em.merge(transaction);

        // -------------------------------------------------------------------------
        // 2. Update related entities
        // -------------------------------------------------------------------------

        CardAccount cardAccount = transaction.getCardAccount();
//        Pedelec pedelec = transaction.getPedelec();

        cardAccount.setInTransaction(false);
        pedelec.setInTransaction(false);
        pedelec.setStationSlot(slot);
        slot.setIsOccupied(true);
        slot.setPedelec(pedelec);

        em.merge(cardAccount);
        em.merge(pedelec);
        em.merge(slot);

        return mergedTransaction;
    }
}
