package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.domain.CardAccount_;
import de.rwth.idsg.bikeman.domain.Customer_;
import de.rwth.idsg.bikeman.domain.MajorCustomer_;
import de.rwth.idsg.bikeman.domain.Pedelec_;
import de.rwth.idsg.bikeman.domain.StationSlot_;
import de.rwth.idsg.bikeman.domain.Station_;
import de.rwth.idsg.bikeman.domain.Transaction_;
import de.rwth.idsg.bikeman.domain.login.User;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewTransactionDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 27.05.2014
 */
@Repository
@Slf4j
public class TransactionRepositoryImpl implements TransactionRepository {

    private enum FindType { ALL, CLOSED, BY_PEDELEC_ID, BY_LOGIN }

    @PersistenceContext private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findAll() throws DatabaseException {
        List<ViewTransactionDTO> list = findAllCustomerTransactions();
        list.addAll(findAllMajorCustomerTransactions());
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findClosed() throws DatabaseException {
        List<ViewTransactionDTO> list = findClosedCustomerTransactions();
        list.addAll(findClosedMajorCustomerTransactions());
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findOpen() throws DatabaseException {
        List<ViewTransactionDTO> list = findOpenCustomerTransactions();
        list.addAll(findOpenMajorCustomerTransactions());
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findAllCustomerTransactions() throws DatabaseException {
        return internalFindCustomer(FindType.ALL);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findClosedCustomerTransactions() throws DatabaseException {
        return internalFindCustomer(FindType.CLOSED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findOpenCustomerTransactions() throws DatabaseException {
        return findCustomerTransactions(em.getCriteriaBuilder());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findAllMajorCustomerTransactions() throws DatabaseException {
        return internalFindMajorCustomer(FindType.ALL);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findClosedMajorCustomerTransactions() throws DatabaseException {
        return internalFindMajorCustomer(FindType.CLOSED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findOpenMajorCustomerTransactions() throws DatabaseException {
        return findMajorCustomerTransactions(em.getCriteriaBuilder());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findByPedelecId(Long pedelecId, Integer resultSize) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        return setResultSizeAndGet(
                em.createQuery(getCustomerTransactionQuery(builder, FindType.BY_PEDELEC_ID, pedelecId, null)),
                em.createQuery(getMajorCustomerTransactionQuery(builder, FindType.BY_PEDELEC_ID, pedelecId, null)),
                resultSize
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findCustomerTransactionsByPedelecId(Long pedelecId, Integer resultSize)
            throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        return setResultSizeAndGet(
                em.createQuery(getCustomerTransactionQuery(builder, FindType.BY_PEDELEC_ID, pedelecId, null)),
                resultSize
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findMajorCustomerTransactionsByPedelecId(Long pedelecId, Integer resultSize)
            throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        return setResultSizeAndGet(
                em.createQuery(getMajorCustomerTransactionQuery(builder, FindType.BY_PEDELEC_ID, pedelecId, null)),
                resultSize
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findByLogin(String login, Integer resultSize) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        return setResultSizeAndGet(
                em.createQuery(getCustomerTransactionQuery(builder, FindType.BY_LOGIN, null, login)),
                em.createQuery(getMajorCustomerTransactionQuery(builder, FindType.BY_LOGIN, null, login)),
                resultSize
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findCustomerTransactionsByLogin(String login, Integer resultSize)
            throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        return setResultSizeAndGet(
                em.createQuery(getCustomerTransactionQuery(builder, FindType.BY_LOGIN, null, login)),
                resultSize
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findMajorCustomerTransactionsByLogin(String login, Integer resultSize)
            throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        return setResultSizeAndGet(
                em.createQuery(getMajorCustomerTransactionQuery(builder, FindType.BY_LOGIN, null, login)),
                resultSize
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction findOpenByPedelecId(Long pedelecId) {
        // TODO
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction start(StartTransactionDTO dto) throws DatabaseException {

        // -------------------------------------------------------------------------
        // 1. Start transaction
        // -------------------------------------------------------------------------

        final String pedelecQuery = "SELECT p FROM Pedelec p WHERE p.manufacturerId = :pedelecManufacturerId";

        final String cardAccQuery = "SELECT ca FROM CardAccount ca WHERE ca.cardId = :cardId";

        final String bookingQuery = "SELECT b FROM Booking b INNER JOIN b.reservation r " +
                    "WHERE r.cardAccount.cardAccountId = :cardAccountId " +
                    "AND r.pedelec.pedelecId = :pedelecId " +
                    "AND r.startDateTime <= :transTime " +
                    "AND :transTime <= r.endDateTime";

        Pedelec pedelec = em.createQuery(pedelecQuery, Pedelec.class)
                            .setParameter("pedelecManufacturerId", dto.getPedelecManufacturerId())
                            .getSingleResult();

        CardAccount cardAccount = em.createQuery(cardAccQuery, CardAccount.class)
                                    .setParameter("cardId", dto.getCardId())
                                    .getSingleResult();

        // check for existing reservation -> get corresponding booking
        LocalDateTime dt = new LocalDateTime(Utils.toMillis(dto.getTimestamp()));
        Booking booking = null;
        try {
            booking = em.createQuery(bookingQuery, Booking.class)
                    .setParameter("cardAccountId", cardAccount.getCardAccountId())
                    .setParameter("pedelecId", pedelec.getPedelecId())
                    .setParameter("transTime", dt)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.debug("No booking found for pedelec {}", dto.getPedelecManufacturerId());
        }

        User user = cardAccount.getUser();
        StationSlot slot = pedelec.getStationSlot();

        // Check integrity of station slot
        String entitySlotManId = slot.getManufacturerId();
        String dtoSlotManId = dto.getSlotManufacturerId();
        boolean slotOK = entitySlotManId.equalsIgnoreCase(dtoSlotManId);
        if (!slotOK) {
            throw new PsException("Integrity check of station slot manufacturerId failed: " +
                    dtoSlotManId  + " (sent from station) != " + entitySlotManId + " (DB value)", PsErrorCode.CONSTRAINT_FAILED);
        }

        // Check integrity of station
        String entityStationManId = slot.getStation().getManufacturerId();
        String dtoStationManId = dto.getStationManufacturerId();
        boolean stationOK = entityStationManId.equalsIgnoreCase(dtoStationManId);
        if (!stationOK) {
            throw new PsException("Integrity check of station manufacturerId failed: " +
                    dtoStationManId  + " (sent from station) != " + entityStationManId + " (DB value)", PsErrorCode.CONSTRAINT_FAILED);
        }

        Transaction transaction = new Transaction();
        transaction.setStartDateTime(new LocalDateTime(dto.getTimestamp()));
        transaction.setCardAccount(cardAccount);
        transaction.setPedelec(pedelec);
        transaction.setFromSlot(slot);
        transaction.setBookedTariff(cardAccount.getCurrentTariff());
        em.persist(transaction);

        // -------------------------------------------------------------------------
        // 2. Update related entities
        // -------------------------------------------------------------------------

        if (user instanceof Customer) {
            ((Customer) user).setInTransaction(true);
        }

        cardAccount.setInTransaction(true);
        pedelec.setInTransaction(true);
        pedelec.setStationSlot(null);
        slot.setIsOccupied(false);
        slot.setPedelec(null);

        em.merge(user);
        em.merge(cardAccount);
        em.merge(pedelec);
        em.merge(slot);

        // update booking
        if (booking == null) {
            // no reservation present: create new booking for transaction
            booking = new Booking();
        }
        booking.setTransaction(transaction);
        em.merge(booking);

        return transaction;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction stop(StopTransactionDTO dto) throws DatabaseException {

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

        Transaction transaction = em.createQuery(endQuery, Transaction.class)
                                    .setParameter("pedelecManufacturerId", dto.getPedelecManufacturerId())
                                    .getSingleResult();

        StationSlot slot = em.createQuery(slotQuery, StationSlot.class)
                             .setParameter("slotManufacturerId", dto.getSlotManufacturerId())
                             .setParameter("stationManufacturerId", dto.getStationManufacturerId())
                             .getSingleResult();

        transaction.setEndDateTime(new LocalDateTime(dto.getTimestamp()));
        transaction.setToSlot(slot);
        Transaction mergedTransaction = em.merge(transaction);

        // -------------------------------------------------------------------------
        // 2. Update related entities
        // -------------------------------------------------------------------------

        CardAccount cardAccount = transaction.getCardAccount();
        Pedelec pedelec = transaction.getPedelec();
        User user = cardAccount.getUser();

        if (user instanceof Customer) {
            ((Customer) user).setInTransaction(false);
        }

        cardAccount.setInTransaction(false);
        pedelec.setInTransaction(false);
        pedelec.setStationSlot(slot);
        slot.setIsOccupied(true);
        slot.setPedelec(pedelec);

        em.merge(cardAccount);
        em.merge(user);
        em.merge(pedelec);
        em.merge(slot);

        return mergedTransaction;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private List<ViewTransactionDTO> internalFindCustomer(FindType findType) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        try {
            return em.createQuery(getCustomerTransactionQuery(builder, findType, null, null)).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    private List<ViewTransactionDTO> internalFindMajorCustomer(FindType findType) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        try {
            return em.createQuery(getMajorCustomerTransactionQuery(builder, findType, null, null)).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    private List<ViewTransactionDTO> setResultSizeAndGet(TypedQuery<ViewTransactionDTO> tqc, Integer resultSize) {
        if (resultSize != null) {
            tqc.setMaxResults(resultSize);
        }

        try {
            return tqc.getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    private List<ViewTransactionDTO> setResultSizeAndGet(
            TypedQuery<ViewTransactionDTO> tqc, TypedQuery<ViewTransactionDTO> tqmc, Integer resultSize) {
        if (resultSize != null) {
            tqc.setMaxResults(resultSize/2);
            tqmc.setMaxResults(resultSize/2);
        }

        try {
            List<ViewTransactionDTO> list = tqc.getResultList();
            list.addAll(tqmc.getResultList());
            return list;
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<ViewTransactionDTO> findMajorCustomerTransactions(CriteriaBuilder builder) throws DatabaseException {
        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> transaction = criteria.from(Transaction.class);

        Join<Transaction, Pedelec> pedelec = transaction.join(Transaction_.pedelec, JoinType.LEFT);

        Join<Transaction, StationSlot> fromStationSlot = transaction.join(Transaction_.fromSlot, JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromStationSlot.join(StationSlot_.station, JoinType.LEFT);

        Join<Transaction, CardAccount> cardAccount = transaction.join(Transaction_.cardAccount, JoinType.LEFT);
        Join majorCustomer = cardAccount.join(CardAccount_.user, JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewTransactionDTO.class,
                        transaction.get(Transaction_.transactionId),
                        transaction.get(Transaction_.startDateTime),
                        fromStation.get(Station_.stationId),
                        fromStation.get(Station_.name),
                        fromStationSlot.get(StationSlot_.stationSlotPosition),
                        cardAccount.get(CardAccount_.cardId),
                        majorCustomer.get(MajorCustomer_.name),
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId)
                )
        ).where(
                builder.and(
                        builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.MAJOR_CUSTOMER),
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

    @SuppressWarnings("unchecked")
    private List<ViewTransactionDTO> findCustomerTransactions(CriteriaBuilder builder) throws DatabaseException {
        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> transaction = criteria.from(Transaction.class);

        Join<Transaction, Pedelec> pedelec = transaction.join(Transaction_.pedelec, JoinType.LEFT);

        Join<Transaction, StationSlot> fromStationSlot = transaction.join(Transaction_.fromSlot, JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromStationSlot.join(StationSlot_.station, JoinType.LEFT);

        Join<Transaction, CardAccount> cardAccount = transaction.join(Transaction_.cardAccount, JoinType.LEFT);
        Join customer = cardAccount.join(CardAccount_.user, JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewTransactionDTO.class,
                        transaction.get(Transaction_.transactionId),
                        transaction.get(Transaction_.startDateTime),
                        fromStation.get(Station_.stationId),
                        fromStation.get(Station_.name),
                        fromStationSlot.get(StationSlot_.stationSlotPosition),
                        cardAccount.get(CardAccount_.cardId),
                        customer.get(Customer_.customerId),
                        customer.get(Customer_.firstname),
                        customer.get(Customer_.lastname),
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId)
                )
        ).where(
                builder.and(
                        builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.CUSTOMER),
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

    @SuppressWarnings("unchecked")
    private CriteriaQuery<ViewTransactionDTO> getCustomerTransactionQuery(CriteriaBuilder builder, FindType findType, Long pedelecId, String login) {
        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> transaction = criteria.from(Transaction.class);

        Join<Transaction, Pedelec> pedelec = transaction.join(Transaction_.pedelec, JoinType.LEFT);

        Join<Transaction, StationSlot> fromStationSlot = transaction.join(Transaction_.fromSlot, JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromStationSlot.join(StationSlot_.station, JoinType.LEFT);

        Join<Transaction, CardAccount> cardAccount = transaction.join(Transaction_.cardAccount, JoinType.LEFT);
        Join customer = cardAccount.join(CardAccount_.user, JoinType.LEFT);

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
                        cardAccount.get(CardAccount_.cardId),
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
                criteria.where(builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.CUSTOMER));
                break;

            case CLOSED:
                criteria.where(
                        builder.and(
                                builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.CUSTOMER),
                                builder.isNotNull(transaction.get(Transaction_.toSlot)),
                                builder.isNotNull(transaction.get(Transaction_.endDateTime))
                        )
                );
                break;

            case BY_PEDELEC_ID:
                criteria.where(
                        builder.and(
                                builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.CUSTOMER),
                                builder.equal(pedelec.get(Pedelec_.pedelecId), pedelecId)
                        )
                );
                break;

            case BY_LOGIN:
                criteria.where(
                        builder.and(
                                builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.CUSTOMER),
                                builder.equal(customer.get(Customer_.login), login)
                        )
                );
                break;
        }

        return criteria;
    }

    @SuppressWarnings("unchecked")
    private CriteriaQuery<ViewTransactionDTO> getMajorCustomerTransactionQuery(CriteriaBuilder builder, FindType findType, Long pedelecId, String login) {
        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> transaction = criteria.from(Transaction.class);

        Join<Transaction, Pedelec> pedelec = transaction.join(Transaction_.pedelec, JoinType.LEFT);

        Join<Transaction, CardAccount> cardAccount = transaction.join(Transaction_.cardAccount, JoinType.LEFT);
        Join majorCustomer = cardAccount.join(CardAccount_.user, JoinType.LEFT);

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
                        cardAccount.get(CardAccount_.cardId),
                        majorCustomer.get(MajorCustomer_.name),
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId)
                )
        ).orderBy(
                builder.desc(transaction.get(Transaction_.endDateTime))
        );

        switch (findType) {
            case ALL:
                criteria.where(
                        builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.MAJOR_CUSTOMER)
                );
                break;

            case CLOSED:
                criteria.where(
                        builder.and(
                                builder.isNotNull(transaction.get(Transaction_.toSlot)),
                                builder.isNotNull(transaction.get(Transaction_.endDateTime)),
                                builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.MAJOR_CUSTOMER)
                        )
                );
                break;

            case BY_PEDELEC_ID:
                criteria.where(
                        builder.and(
                        builder.equal(pedelec.get(Pedelec_.pedelecId), pedelecId),
                        builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.MAJOR_CUSTOMER)
                        )
                );
                break;

            case BY_LOGIN:
                criteria.where(
                        builder.and(
                        builder.equal(majorCustomer.get(MajorCustomer_.login), login),
                        builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.MAJOR_CUSTOMER)
                        )
                );
                break;
        }

        return criteria;
    }
}
