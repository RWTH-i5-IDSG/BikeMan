package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.*;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewTransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    private enum FindType { ALL, CLOSED };

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ViewTransactionDTO> findAll() {
        return em.createQuery(
                getTransactionQuery(FindType.ALL)
        ).getResultList();
    }

    @Override
    public List<ViewTransactionDTO> findClosed() {
        return em.createQuery(
                getTransactionQuery(FindType.CLOSED)
        ).getResultList();
    }

    @Override
    public List<ViewTransactionDTO> findOpen() {
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
        ).where(builder.isNull(root.get("toSlot")));

        return em.createQuery(criteria).getResultList();
    }

    @Override
    public Transaction findOpenByPedelecId(Long pedelecId) {
        return null;
    }

    @Override
    public void start(Transaction transaction) {

    }

    @Override
    public void stop(Transaction transaction) {

    }

    private CriteriaQuery<ViewTransactionDTO> getTransactionQuery(FindType findType) {
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
                criteria.where(builder.isNotNull(root.get("toSlot")));
                break;
        }

        return criteria;
    }
}
