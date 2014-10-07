package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.domain.Customer_;
import de.rwth.idsg.bikeman.domain.Pedelec_;
import de.rwth.idsg.bikeman.domain.StationSlot_;
import de.rwth.idsg.bikeman.domain.Station_;
import de.rwth.idsg.bikeman.domain.Transaction_;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewTransactionDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by sgokay on 27.05.14.
 */
@Repository
@Slf4j
public class TransactionRepositoryImpl implements TransactionRepository {

    private enum FindType { ALL, CLOSED, BY_PEDELEC_ID, BY_LOGIN };

    @PersistenceContext private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findAll() throws DatabaseException {
        try {
            return em.createQuery(
                    getTransactionQuery(FindType.ALL, null, null)
            ).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findClosed() throws DatabaseException {
        try {
            return em.createQuery(
                    getTransactionQuery(FindType.CLOSED, null, null)
            ).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findByPedelecId(Long pedelecId, Integer resultSize) throws DatabaseException {
        try {
            TypedQuery<ViewTransactionDTO> tq = em.createQuery(
                    getTransactionQuery(FindType.BY_PEDELEC_ID, pedelecId, null)
            );

            if (resultSize != null) {
                tq.setMaxResults(resultSize);
            }
            return tq.getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findByLogin(String login, Integer resultSize) throws DatabaseException {
        try {
            TypedQuery<ViewTransactionDTO> tq = em.createQuery(
                    getTransactionQuery(FindType.BY_LOGIN, null, login)
            );

            if (resultSize != null) {
                tq.setMaxResults(resultSize);
            }
            return tq.getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findOpen() throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> transaction = criteria.from(Transaction.class);

        Join<Transaction, Pedelec> pedelec = transaction.join(Transaction_.pedelec, JoinType.LEFT);
        Join<Transaction, Customer> customer = transaction.join(Transaction_.customer, JoinType.LEFT);

        Join<Transaction, StationSlot> fromStationSlot = transaction.join(Transaction_.fromSlot, JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromStationSlot.join(StationSlot_.station, JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewTransactionDTO.class,
                        transaction.get(Transaction_.transactionId),
                        transaction.get(Transaction_.startDateTime),
                        fromStation.get(Station_.stationId),
                        fromStation.get(Station_.name),
                        fromStationSlot.get(StationSlot_.stationSlotPosition),
                        customer.get(Customer_.customerId),
                        customer.get(Customer_.firstname),
                        customer.get(Customer_.lastname),
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId)
                )
        ).where(
                builder.and(
                        builder.isNull(transaction.get(Transaction_.toSlot)),
                        builder.isNull(transaction.get(Transaction_.endDateTime))
                )
        );

        try {
            return em.createQuery(criteria).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction findOpenByPedelecId(Long pedelecId) {
        // TODO
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void start(StartTransactionDTO dto) throws DatabaseException {

        // -------------------------------------------------------------------------
        // 1. Start transaction
        // -------------------------------------------------------------------------

        final String startQuery = "SELECT p FROM Pedelec p WHERE p.manufacturerId = :pedelecManufacturerId";

        Pedelec pedelec = (Pedelec) em.createQuery(startQuery)
                                      .setParameter("pedelecManufacturerId", dto.getPedelecManufacturerId())
                                      .getSingleResult();

        Customer customer = em.find(Customer.class, dto.getUserId());
        StationSlot slot = pedelec.getStationSlot();

        // Check integrity of station slot
        String entitySlotManId = slot.getManufacturerId();
        String dtoSlotManId = dto.getSlotManufacturerId();
        boolean slotOK = entitySlotManId.equalsIgnoreCase(dtoSlotManId);
        if (!slotOK) {
            throw new DatabaseException("Integrity check of station slot manufacturerId failed: FromDTO[" +
                    dtoSlotManId + "] != FromDB[" + entitySlotManId + "]");
        }

        // Check integrity of station
        String entityStationManId = slot.getStation().getManufacturerId();
        String dtoStationManId = dto.getStationManufacturerId();
        boolean stationOK = entityStationManId.equalsIgnoreCase(dtoStationManId);
        if (!stationOK) {
            throw new DatabaseException("Integrity check of station manufacturerId failed: FromDTO[" +
                    dtoStationManId + "] != FromDB[" + entityStationManId + "]");
        }

        Transaction transaction = new Transaction();
        transaction.setStartDateTime(new LocalDateTime(dto.getTimestamp()));
        transaction.setCustomer(customer);
        transaction.setPedelec(pedelec);
        transaction.setFromSlot(slot);
        em.persist(transaction);

        // -------------------------------------------------------------------------
        // 2. Update related entities
        // -------------------------------------------------------------------------

        customer.setInTransaction(true);
        pedelec.setInTransaction(true);
        pedelec.setStationSlot(null);
        slot.setIsOccupied(false);
        slot.setPedelec(null);

        em.merge(customer);
        em.merge(pedelec);
        em.merge(slot);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stop(StopTransactionDTO dto) throws DatabaseException {

        // -------------------------------------------------------------------------
        // 1. End transaction
        // -------------------------------------------------------------------------

        final String endQuery = "SELECT t FROM Transaction t " +
                                "WHERE t.pedelec = (SELECT p FROM Pedelec p WHERE p.manufacturerId = :pedelecManufacturerId) " +
                                "AND t.toSlot IS NULL " +
                                "AND t.endDateTime IS NULL";

        final String slotQuery = "SELECT ss FROM StationSlot ss " +
                                 "WHERE ss.manufacturerId = :slotManufacturerId " +
                                 "AND ss.station = (SELECT s FROM Station s WHERE s.manufacturerId = :stationManufacturerId)";

        Transaction transaction = (Transaction) em.createQuery(endQuery)
                                                  .setParameter("pedelecManufacturerId", dto.getPedelecManufacturerId())
                                                  .getSingleResult();

        StationSlot slot = (StationSlot) em.createQuery(slotQuery)
                                           .setParameter("slotManufacturerId", dto.getSlotManufacturerId())
                                           .setParameter("stationManufacturerId", dto.getStationManufacturerId())
                                           .getSingleResult();

        transaction.setEndDateTime(new LocalDateTime(dto.getTimestamp()));
        transaction.setToSlot(slot);
        em.merge(transaction);

        // -------------------------------------------------------------------------
        // 2. Update related entities
        // -------------------------------------------------------------------------

        Customer customer = transaction.getCustomer();
        Pedelec pedelec = transaction.getPedelec();

        customer.setInTransaction(false);
        pedelec.setInTransaction(false);
        pedelec.setStationSlot(slot);
        slot.setIsOccupied(true);
        slot.setPedelec(pedelec);

        em.merge(customer);
        em.merge(pedelec);
        em.merge(slot);
    }

    private CriteriaQuery<ViewTransactionDTO> getTransactionQuery(FindType findType, Long pedelecId, String login) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> transaction = criteria.from(Transaction.class);

        Join<Transaction, Pedelec> pedelec = transaction.join(Transaction_.pedelec, JoinType.LEFT);
        Join<Transaction, Customer> customer = transaction.join(Transaction_.customer, JoinType.LEFT);

        Join<Transaction, StationSlot> fromStationSlot = transaction.join(Transaction_.fromSlot, JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromStationSlot.join(StationSlot_.station, JoinType.LEFT);

        Join<Transaction, StationSlot> toStationSlot = transaction.join(Transaction_.toSlot, JoinType.LEFT);
        Join<StationSlot, Station> toStation = toStationSlot.join(StationSlot_.station, JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewTransactionDTO.class,
                        transaction.get(Transaction_.transactionId),
                        transaction.get(Transaction_.startDateTime),
                        transaction.get(Transaction_.endDateTime),
                        fromStation.get(Station_.stationId),
                        fromStation.get(Station_.name),
                        fromStationSlot.get(StationSlot_.stationSlotPosition),
                        toStation.get(Station_.stationId),
                        toStation.get(Station_.name),
                        toStationSlot.get(StationSlot_.stationSlotPosition),
                        customer.get(Customer_.customerId),
                        customer.get(Customer_.firstname),
                        customer.get(Customer_.lastname),
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId)
                )
        ).orderBy(
                builder.desc(transaction.get(Transaction_.endDateTime))
        );

        switch (findType) {
            case ALL:
                break;

            case CLOSED:
                criteria.where(
                        builder.and(
                                builder.isNotNull(transaction.get(Transaction_.toSlot)),
                                builder.isNotNull(transaction.get(Transaction_.endDateTime))
                        )
                );
                break;

            case BY_PEDELEC_ID:
                criteria.where(
                        builder.equal(pedelec.get(Pedelec_.pedelecId), pedelecId)
                );
                break;

            case BY_LOGIN:
                criteria.where(
                        builder.equal(customer.get(Customer_.login), login)
                );
                break;
        }

        return criteria;
    }
}
