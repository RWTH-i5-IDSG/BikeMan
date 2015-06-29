package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.domain.CardAccount_;
import de.rwth.idsg.bikeman.domain.Customer_;
import de.rwth.idsg.bikeman.domain.MajorCustomer_;
import de.rwth.idsg.bikeman.domain.Pedelec_;
import de.rwth.idsg.bikeman.domain.StationSlot_;
import de.rwth.idsg.bikeman.domain.Station_;
import de.rwth.idsg.bikeman.domain.Transaction_;
import de.rwth.idsg.bikeman.domain.User_;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewTransactionDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
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
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 27.05.2014
 */
@Repository
@Slf4j
public class TransactionRepositoryImpl implements TransactionRepository {

    private enum FindType {
        ALL, CLOSED, OPEN,
        BY_PEDELEC_ID, BY_LOGIN
    }

    @PersistenceContext private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findAllCustomerTransactions() throws DatabaseException {
        return internalFind(FindType.ALL, CustomerType.CUSTOMER);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findClosedCustomerTransactions() throws DatabaseException {
        return internalFind(FindType.CLOSED, CustomerType.CUSTOMER);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findOpenCustomerTransactions() throws DatabaseException {
        return internalFind(FindType.OPEN, CustomerType.CUSTOMER);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findAllMajorCustomerTransactions() throws DatabaseException {
        return internalFind(FindType.ALL, CustomerType.MAJOR_CUSTOMER);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findClosedMajorCustomerTransactions() throws DatabaseException {
        return internalFind(FindType.CLOSED, CustomerType.MAJOR_CUSTOMER);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findOpenMajorCustomerTransactions() throws DatabaseException {
        return internalFind(FindType.OPEN, CustomerType.MAJOR_CUSTOMER);
    }

    @Override
    public List<ViewTransactionDTO> findTransactionsByPedelecId(Long pedelecId, Integer resultSize) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        List<ViewTransactionDTO> returnList =
            setResultSizeAndGet(
                em.createQuery(getTransactionQuery(builder, FindType.BY_PEDELEC_ID, CustomerType.MAJOR_CUSTOMER, pedelecId, null)),
                resultSize
            );

        returnList.addAll(
            setResultSizeAndGet(
                em.createQuery(getTransactionQuery(builder, FindType.BY_PEDELEC_ID, CustomerType.CUSTOMER, pedelecId, null)),
                resultSize
            ));

        return returnList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findCustomerTransactionsByLogin(String login, Integer resultSize)
        throws DatabaseException {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        return setResultSizeAndGet(
            em.createQuery(getTransactionQuery(builder, FindType.BY_LOGIN, CustomerType.CUSTOMER, null, login)),
            resultSize
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> findMajorCustomerTransactionsByLogin(String login, Integer resultSize)
        throws DatabaseException {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        return setResultSizeAndGet(
            em.createQuery(getTransactionQuery(builder, FindType.BY_LOGIN, CustomerType.MAJOR_CUSTOMER, null, login)),
            resultSize
        );
    }

    /**
     * Delete booking and transaction and set all related DB entries to default.
     *
     * As if it did not exist.
     *
     * ONLY FOR DEBUGGING!
     */
    @Override
    @Transactional(readOnly = false)
    public void kill(long transactionId) {

        Transaction transaction = em.find(Transaction.class, transactionId);

        em.createQuery("DELETE FROM Booking b WHERE b.transaction = :transaction")
          .setParameter("transaction", transaction)
          .executeUpdate();

        em.createQuery("UPDATE CardAccount ca SET ca.inTransaction = false WHERE ca = :cardAccount")
          .setParameter("cardAccount", transaction.getCardAccount())
          .executeUpdate();

        em.createQuery("UPDATE Pedelec p SET p.inTransaction = false WHERE p = :pedelec")
          .setParameter("pedelec", transaction.getPedelec())
          .executeUpdate();

        // Register the pedelec back at the starting station slot
        em.createQuery("UPDATE StationSlot ss SET ss.isOccupied = true, ss.pedelec = :pedelec WHERE ss = :stationSlot")
          .setParameter("pedelec", transaction.getPedelec())
          .setParameter("stationSlot", transaction.getFromSlot())
          .executeUpdate();

        em.remove(transaction);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private List<ViewTransactionDTO> internalFind(FindType findType, CustomerType customerType) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        try {
            return em.createQuery(getTransactionQuery(builder, findType, customerType, null, null)).getResultList();
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
    private CriteriaQuery<ViewTransactionDTO> getTransactionQuery(CriteriaBuilder builder,
                                                                  FindType findType, CustomerType customerType,
                                                                  Long pedelecId, String login) {

        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> transaction = criteria.from(Transaction.class);

        Join<Transaction, Pedelec> pedelec = transaction.join(Transaction_.pedelec, JoinType.LEFT);

        Join<Transaction, StationSlot> fromStationSlot = transaction.join(Transaction_.fromSlot, JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromStationSlot.join(StationSlot_.station, JoinType.LEFT);

        Join<Transaction, CardAccount> cardAccount = transaction.join(Transaction_.cardAccount, JoinType.LEFT);
        Join user = cardAccount.join(CardAccount_.user, JoinType.LEFT);

        Join<Transaction, StationSlot> toStationSlot = transaction.join(Transaction_.toSlot, JoinType.LEFT);
        Join<StationSlot, Station> toStation = toStationSlot.join(StationSlot_.station, JoinType.LEFT);

        // -------------------------------------------------------------------------
        // Customer type decisions
        // -------------------------------------------------------------------------

        switch (customerType) {
            case MAJOR_CUSTOMER:
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
                        user.get(MajorCustomer_.name),
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId)
                    )
                ).orderBy(
                    builder.desc(transaction.get(Transaction_.endDateTime))
                );
                break;

            case CUSTOMER:
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
                        user.get(Customer_.customerId),
                        user.get(Customer_.firstname),
                        user.get(Customer_.lastname),
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId)
                    )
                ).orderBy(
                    builder.desc(transaction.get(Transaction_.endDateTime))
                );
                break;
        }

        // -------------------------------------------------------------------------
        // Find type decisions
        // -------------------------------------------------------------------------

        switch (findType) {
            case ALL:
                criteria.where(
                    builder.equal(cardAccount.get(CardAccount_.ownerType), customerType)
                );
                break;

            case CLOSED:
                criteria.where(
                    builder.and(
                        builder.equal(cardAccount.get(CardAccount_.ownerType), customerType),
                        builder.isNotNull(transaction.get(Transaction_.toSlot)),
                        builder.isNotNull(transaction.get(Transaction_.endDateTime))
                    )
                );
                break;

            case OPEN:
                criteria.where(
                    builder.and(
                        builder.equal(cardAccount.get(CardAccount_.ownerType), customerType),
                        builder.isNull(transaction.get(Transaction_.toSlot)),
                        builder.isNull(transaction.get(Transaction_.endDateTime))
                    )
                );
                break;

            case BY_PEDELEC_ID:
                criteria.where(
                    builder.and(
                        builder.equal(cardAccount.get(CardAccount_.ownerType), customerType),
                        builder.equal(pedelec.get(Pedelec_.pedelecId), pedelecId)
                    )
                );
                break;

            case BY_LOGIN:
                criteria.where(
                    builder.and(
                        builder.equal(cardAccount.get(CardAccount_.ownerType), customerType),
                        builder.equal(user.get(User_.login), login)
                    )
                );
                break;
        }

        return criteria;
    }
}
