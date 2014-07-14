package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.*;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewTransactionDTO;
import lombok.extern.slf4j.Slf4j;
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

    @PersistenceContext
    EntityManager em;

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
    public void start(Transaction transaction) {
        // TODO
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stop(Transaction transaction) {
        // TODO
    }

    private CriteriaQuery<ViewTransactionDTO> getTransactionQuery(FindType findType, Long pedelecId, String login) {
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
        ).orderBy(
                builder.desc(root.get("endDateTime"))
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
                );
                break;

            case BY_LOGIN:
                criteria.where(
                        builder.equal(customerJoin.get("login"), login)
                );
                break;
        }

        return criteria;
    }
}
