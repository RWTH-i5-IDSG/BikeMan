package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.*;
import de.rwth.idsg.velocity.web.rest.BackendException;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewTransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sgokay on 27.05.14.
 */
@Repository
@Transactional
public class TransactionRepositoryImpl implements TransactionRepository {

    private static final Logger log = LoggerFactory.getLogger(TransactionRepositoryImpl.class);

    private enum FindType { ALL, CLOSED, BY_PEDELEC_ID };

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ViewTransactionDTO> findAll() throws BackendException {
        try {
            return em.createQuery(
                    getTransactionQuery(FindType.ALL, null)
            ).getResultList();
        } catch (Exception e) {
            throw new BackendException("Failed during database operation.");
        }
    }

    @Override
    public List<ViewTransactionDTO> findClosed() throws BackendException {
        try {
            return em.createQuery(
                    getTransactionQuery(FindType.CLOSED, null)
            ).getResultList();
        } catch (Exception e) {
            throw new BackendException("Failed during database operation.");
        }
    }

    @Override
    public List<ViewTransactionDTO> findByPedelecId(Long pedelecId, Integer resultSize) throws BackendException {
        try {
            TypedQuery<ViewTransactionDTO> tq = em.createQuery(
                    getTransactionQuery(FindType.BY_PEDELEC_ID, pedelecId)
            );

            tq.setMaxResults(resultSize);
            return tq.getResultList();
        } catch (Exception e) {
            throw new BackendException("Failed during database operation.");
        }
    }

    @Override
    public List<ViewTransactionDTO> findOpen() throws BackendException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> root = criteria.from(Transaction.class);

        Join<Transaction, Pedelec> pedelecJoin = root.join("pedelec", JoinType.LEFT);
        Join<Transaction, Customer> customerJoin = root.join("customer", JoinType.LEFT);

        Join<Transaction, StationSlot> fromSlotJoin = root.join("fromSlot", JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromSlotJoin.join("station", JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewTransactionDTO.class,
                        root.get("transactionId"),
                        root.get("startDateTime"),
                        fromStation.get("stationId"),
                        fromStation.get("name"),
                        fromSlotJoin.get("stationSlotPosition"),
                        customerJoin.get("customerId"),
                        customerJoin.get("firstname"),
                        customerJoin.get("lastname"),
                        pedelecJoin.get("pedelecId"),
                        pedelecJoin.get("manufacturerId")
                )
        ).where(
                builder.and(
                        builder.isNull(root.get("toSlot")),
                        builder.isNull(root.get("endDateTime"))
                )
        );

        try {
            return em.createQuery(criteria).getResultList();
        } catch (Exception e) {
            throw new BackendException("Failed during database operation.");
        }
    }

    @Override
    public Transaction findOpenByPedelecId(Long pedelecId) {
        // TODO
        return null;
    }

    @Override
    public void start(Transaction transaction) {
        // TODO
    }

    @Override
    public void stop(Transaction transaction) {
        // TODO
    }

    private CriteriaQuery<ViewTransactionDTO> getTransactionQuery(FindType findType, Long pedelecId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> root = criteria.from(Transaction.class);

        Join<Transaction, Pedelec> pedelecJoin = root.join("pedelec", JoinType.LEFT);
        Join<Transaction, Customer> customerJoin = root.join("customer", JoinType.LEFT);

        Join<Transaction, StationSlot> fromSlotJoin = root.join("fromSlot", JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromSlotJoin.join("station", JoinType.LEFT);

        Join<Transaction, StationSlot> toSlotJoin = root.join("toSlot", JoinType.LEFT);
        Join<StationSlot, Station> toStation = toSlotJoin.join("station", JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewTransactionDTO.class,
                        root.get("transactionId"),
                        root.get("startDateTime"),
                        root.get("endDateTime"),
                        fromStation.get("stationId"),
                        fromStation.get("name"),
                        fromSlotJoin.get("stationSlotPosition"),
                        toStation.get("stationId"),
                        toStation.get("name"),
                        toSlotJoin.get("stationSlotPosition"),
                        customerJoin.get("customerId"),
                        customerJoin.get("firstname"),
                        customerJoin.get("lastname"),
                        pedelecJoin.get("pedelecId"),
                        pedelecJoin.get("manufacturerId")
                )
        );

        switch (findType) {
            case ALL:
                break;

            case CLOSED:
                criteria.where(
                        builder.and(
                                builder.isNotNull(root.get("toSlot")),
                                builder.isNotNull(root.get("endDateTime"))
                        )
                );
                break;

            case BY_PEDELEC_ID:
                criteria.where(
                        builder.equal(pedelecJoin.get("pedelecId"), pedelecId)
                ).orderBy(
                        builder.desc(root.get("endDateTime"))
                );
                break;
        }

        return criteria;
    }
}
