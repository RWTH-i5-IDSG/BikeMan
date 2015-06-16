package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.domain.CardAccount_;
import de.rwth.idsg.bikeman.domain.Customer_;
import de.rwth.idsg.bikeman.domain.MajorCustomer_;
import de.rwth.idsg.bikeman.domain.Pedelec_;
import de.rwth.idsg.bikeman.domain.StationSlot_;
import de.rwth.idsg.bikeman.domain.Station_;
import de.rwth.idsg.bikeman.domain.Transaction_;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewTransactionDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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
