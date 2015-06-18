package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.ViewTransactionDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.domain.CardAccount_;
import de.rwth.idsg.bikeman.domain.Customer_;
import de.rwth.idsg.bikeman.domain.StationSlot_;
import de.rwth.idsg.bikeman.domain.Station_;
import de.rwth.idsg.bikeman.domain.Transaction_;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

@Repository("TransactionRepositoryImplApp")
@Slf4j
public class TransactionRepositoryImpl implements TransactionRepository {

    @PersistenceContext
    private EntityManager em;


    public List<ViewTransactionDTO> findAllByCustomer(Customer customer) throws AppException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);

        Root<Transaction> transaction = criteria.from(Transaction.class);

        Join<Transaction, CardAccount> cardAccount = transaction.join(Transaction_.cardAccount, JoinType.LEFT);

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
                transaction.get(Transaction_.fees),
                fromStation.get(Station_.stationId),
                fromStation.get(Station_.name),
                toStation.get(Station_.stationId),
                toStation.get(Station_.name)
            )
        ).where(
            builder.and(
                builder.isNotNull(transaction.get(Transaction_.toSlot)),
                builder.isNotNull(transaction.get(Transaction_.endDateTime)),
                builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.CUSTOMER),
                builder.equal(cardAccount.get(CardAccount_.cardAccountId), customer.getCardAccount().getCardAccountId())
            )
        ).orderBy(
            builder.desc(transaction.get(Transaction_.endDateTime))
        );

        try {
            return em.createQuery(criteria).setMaxResults(10).getResultList();
        } catch (Exception e) {
            throw new AppException("Failed during database operation.", AppErrorCode.DATABASE_OPERATION_FAILED);
        }

    }


    public ViewTransactionDTO findOpenByCustomer(Customer customer) throws AppException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);

        Root<Transaction> transaction = criteria.from(Transaction.class);

        Join<Transaction, CardAccount> cardAccount = transaction.join(Transaction_.cardAccount, JoinType.LEFT);

        Join<Transaction, StationSlot> fromStationSlot = transaction.join(Transaction_.fromSlot, JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromStationSlot.join(StationSlot_.station, JoinType.LEFT);

        criteria.select(
            builder.construct(
                ViewTransactionDTO.class,
                transaction.get(Transaction_.transactionId),
                transaction.get(Transaction_.startDateTime),
                fromStation.get(Station_.stationId),
                fromStation.get(Station_.name)
            )
        ).where(
            builder.and(
                builder.isNull(transaction.get(Transaction_.toSlot)),
                builder.isNull(transaction.get(Transaction_.endDateTime)),
                builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.CUSTOMER),
                builder.equal(cardAccount.get(CardAccount_.cardAccountId), customer.getCardAccount().getCardAccountId())
            )
        ).orderBy(
            builder.desc(transaction.get(Transaction_.endDateTime))
        );

        try {
            List <ViewTransactionDTO> result =  em.createQuery(criteria).getResultList();
            if (result.isEmpty()) {
                return null;
            } else {
                return result.get(0);
            }
        } catch (Exception e) {
            throw new AppException("Failed during database operation.", AppErrorCode.DATABASE_OPERATION_FAILED);
        }

    }

    public Long numberOfOpenTransactionsByCustomer(Customer customer) {
        final String openTransactionsQuery = "SELECT COUNT(t) FROM Transaction t " +
            "WHERE t.cardAccount.cardAccountId = :cardAccountId " +
            "AND t.endDateTime IS NULL " +
            "AND t.toSlot IS NULL";

        return em.createQuery(openTransactionsQuery, Long.class)
            .setParameter("cardAccountId", customer.getCardAccount().getCardAccountId())
            .getSingleResult();
    }

}
