package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Transaction;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewTransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sgokay on 27.05.14.
 */
@Repository
@Transactional
public class TransactionRepositoryImpl implements TransactionRepository {

    private static final Logger log = LoggerFactory.getLogger(TransactionRepositoryImpl.class);

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ViewTransactionDTO> findAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> root = criteria.from(Transaction.class);

        criteria.select(
                builder.construct(
                        ViewTransactionDTO.class,
                        root.get("transactionId"),
                        root.get("startDateTime"),
                        root.get("endDateTime"),
                        root.get("fromSlot").get("station").get("stationId"),
                        root.get("fromSlot").get("station").get("name"),
                        root.get("fromSlot").get("stationSlotId"),
                        root.get("toSlot").get("station").get("stationId"),
                        root.get("toSlot").get("station").get("name"),
                        root.get("toSlot").get("stationSlotId"),
                        root.get("customer").get("customerId"),
                        root.get("customer").get("firstname"),
                        root.get("customer").get("lastname"),
                        root.get("pedelec").get("pedelecId"),
                        root.get("pedelec").get("manufacturerId")
                )
        );

        return em.createQuery(criteria).getResultList();
    }

    @Override
    public List<ViewTransactionDTO> findOpen() {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<ViewTransactionDTO> criteria = builder.createQuery(ViewTransactionDTO.class);
        Root<Transaction> root = criteria.from(Transaction.class);

        criteria.select(
                builder.construct(
                        ViewTransactionDTO.class,
                        root.get("transactionId"),
                        root.get("startDateTime"),
                        root.get("fromSlot").get("station").get("stationId"),
                        root.get("fromSlot").get("station").get("name"),
                        root.get("fromSlot").get("stationSlotId"),
                        root.get("customer").get("customerId"),
                        root.get("customer").get("firstname"),
                        root.get("customer").get("lastname"),
                        root.get("pedelec").get("pedelecId"),
                        root.get("pedelec").get("manufacturerId")
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
}
