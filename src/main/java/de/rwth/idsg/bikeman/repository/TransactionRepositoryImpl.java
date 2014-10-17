package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.domain.CardAccount_;
import de.rwth.idsg.bikeman.domain.Customer_;
import de.rwth.idsg.bikeman.domain.MajorCustomer_;
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
import javax.persistence.criteria.*;
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
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            list = em.createQuery(getCustomerTransactionQuery(builder, FindType.ALL, null, null)).getResultList();
            list.addAll(em.createQuery(getMajorCustomerTransactionQuery(builder, FindType.ALL, null, null)).getResultList());
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findAllCustomerTransactions() throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            list = em.createQuery(getCustomerTransactionQuery(builder, FindType.ALL, null, null)).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findAllMajorCustomerTransactions() throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            list = em.createQuery(getMajorCustomerTransactionQuery(builder, FindType.ALL, null, null)).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }


    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findClosed() throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            list = em.createQuery(getCustomerTransactionQuery(builder, FindType.CLOSED, null, null)).getResultList();
            list.addAll(em.createQuery(getMajorCustomerTransactionQuery(builder, FindType.CLOSED, null, null)).getResultList());
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findClosedCustomerTransactions() throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            list = em.createQuery(getCustomerTransactionQuery(builder, FindType.CLOSED, null, null)).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findClosedMajorCustomerTransactions() throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            list = em.createQuery(getMajorCustomerTransactionQuery(builder, FindType.CLOSED, null, null)).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findByPedelecId(Long pedelecId, Integer resultSize) throws DatabaseException {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            TypedQuery<ViewTransactionDTO> tqc = em.createQuery(
                    getCustomerTransactionQuery(builder, FindType.BY_PEDELEC_ID, pedelecId, null)
            );

            TypedQuery<ViewTransactionDTO> tqmc = em.createQuery(
                    getMajorCustomerTransactionQuery(builder, FindType.BY_PEDELEC_ID, pedelecId, null)
            );

            if (resultSize != null) {
                tqc.setMaxResults(resultSize/2);
                tqmc.setMaxResults(resultSize/2);
            }

            list = tqc.getResultList();
            list.addAll(tqmc.getResultList());

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }

    @Override
         @Transactional(readOnly = true)
         public List<ViewTransactionDTO> findCustomerTransactionsByPedelecId(Long pedelecId, Integer resultSize) throws DatabaseException {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            TypedQuery<ViewTransactionDTO> tqc = em.createQuery(
                    getCustomerTransactionQuery(builder, FindType.BY_PEDELEC_ID, pedelecId, null)
            );

            if (resultSize != null) {
                tqc.setMaxResults(resultSize);
            }

            list = tqc.getResultList();

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findMajorCustomerTransactionsByPedelecId(Long pedelecId, Integer resultSize) throws DatabaseException {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            TypedQuery<ViewTransactionDTO> tqc = em.createQuery(
                    getMajorCustomerTransactionQuery(builder, FindType.BY_PEDELEC_ID, pedelecId, null)
            );

            if (resultSize != null) {
                tqc.setMaxResults(resultSize);
            }

            list = tqc.getResultList();

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findByLogin(String login, Integer resultSize) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            TypedQuery<ViewTransactionDTO> tqc = em.createQuery(
                    getCustomerTransactionQuery(builder, FindType.BY_LOGIN, null, login)
            );

            TypedQuery<ViewTransactionDTO> tqmc = em.createQuery(
                    getMajorCustomerTransactionQuery(builder, FindType.BY_LOGIN, null, login)
            );

            if (resultSize != null) {
                tqc.setMaxResults(resultSize/2);
                tqmc.setMaxResults(resultSize/2);
            }

            list = tqc.getResultList();
            list.addAll(tqmc.getResultList());

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findCustomerTransactionsByLogin(String login, Integer resultSize) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            TypedQuery<ViewTransactionDTO> tqc = em.createQuery(
                    getCustomerTransactionQuery(builder, FindType.BY_LOGIN, null, login)
            );

            if (resultSize != null) {
                tqc.setMaxResults(resultSize);
            }

            list = tqc.getResultList();

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findMajorCustomerTransactionsByLogin(String login, Integer resultSize) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = null;

        try {
            TypedQuery<ViewTransactionDTO> tqc = em.createQuery(
                    getMajorCustomerTransactionQuery(builder, FindType.BY_LOGIN, null, login)
            );

            if (resultSize != null) {
                tqc.setMaxResults(resultSize);
            }

            list = tqc.getResultList();

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }

        return list;
    }


    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findOpen() throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = findCustomerTransactions(builder);
        list.addAll(findMajorCustomerTransactions(builder));
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findOpenCustomerTransactions() throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = findCustomerTransactions(builder);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findOpenMajorCustomerTransactions() throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> list = findMajorCustomerTransactions(builder);
        return list;
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

//        Customer customer = em.find(Customer.class, dto.getUserId());

//        CriteriaBuilder builder = em.getCriteriaBuilder();
//        CriteriaQuery<CardAccount> criteria = builder.createQuery(CardAccount.class);
//        Root<CardAccount> cardAccountRoot = criteria.from(CardAccount.class);
//
//        criteria.select(cardAccountRoot).where(builder.equal(cardAccountRoot.get(CardAccount_.cardId), dto.getCardId()));
//        TypedQuery<CardAccount> query = em.createQuery(criteria);

//        CardAccount cardAccount = query.getSingleResult();


        final String cardAccountQuery =  "SELECT ca FROM CardAccount ca WHERE ca.cardId = :cardId";

        CardAccount cardAccount = em.createQuery(cardAccountQuery, CardAccount.class)
                .setParameter("cardId", dto.getCardId())
                .getSingleResult();

        Customer customer = (Customer) cardAccount.getUser();

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
//        transaction.setCustomer(customer);
        transaction.setCardAccount(cardAccount);
        transaction.setPedelec(pedelec);
        transaction.setFromSlot(slot);
        em.persist(transaction);

        // -------------------------------------------------------------------------
        // 2. Update related entities
        // -------------------------------------------------------------------------

        customer.setInTransaction(true);
        cardAccount.setInTransaction(true);
        pedelec.setInTransaction(true);
        pedelec.setStationSlot(null);
        slot.setIsOccupied(false);
        slot.setPedelec(null);

//        em.merge(customer);
        em.merge(cardAccount);
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

        CardAccount cardAccount = transaction.getCardAccount();
//        Customer customer = transaction.getCustomer();
        Customer customer = (Customer)cardAccount.getUser();
        Pedelec pedelec = transaction.getPedelec();

        cardAccount.setInTransaction(false);
        customer.setInTransaction(false);
        pedelec.setInTransaction(false);
        pedelec.setStationSlot(slot);
        slot.setIsOccupied(true);
        slot.setPedelec(pedelec);

        em.merge(cardAccount);
        em.merge(customer);
        em.merge(pedelec);
        em.merge(slot);
    }

    @SuppressWarnings("unchecked")
    private CriteriaQuery<ViewTransactionDTO> getCustomerTransactionQuery(CriteriaBuilder builder, FindType findType, Long pedelecId, String login) {
        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> transaction = criteria.from(Transaction.class);

        Join<Transaction, Pedelec> pedelec = transaction.join(Transaction_.pedelec, JoinType.LEFT);

        Join<Transaction, CardAccount> cardAccount = transaction.join(Transaction_.cardAccount, JoinType.LEFT);
        Join customer = cardAccount.join(CardAccount_.user, JoinType.LEFT);

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
