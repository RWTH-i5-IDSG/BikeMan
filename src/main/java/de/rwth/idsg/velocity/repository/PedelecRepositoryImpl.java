package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.*;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewPedelecDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sgokay on 21.05.14.
 */
@Repository
@Transactional
public class PedelecRepositoryImpl implements PedelecRepository {

    private static final Logger log = LoggerFactory.getLogger(PedelecRepositoryImpl.class);

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ViewPedelecDTO> findAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        return em.createQuery(
                getQuery(builder, null)
        ).getResultList();
    }

    @Override
    public ViewPedelecDTO findOneDTO(Long pedelecId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        return em.createQuery(
                getQuery(builder, pedelecId)
        ).getSingleResult();
    }

    @Override
    public Pedelec findOne(long pedelecId) {
        return em.find(Pedelec.class, pedelecId);
    }

    @Override
    public void create(CreateEditPedelecDTO dto) {
        Pedelec pedelec = new Pedelec();
        setFields(pedelec, dto);
        em.persist(pedelec);
        log.debug("Created new pedelec {}", pedelec);
    }

    @Override
    public void update(CreateEditPedelecDTO dto) {
        final Long pedelecId = dto.getPedelecId();
        if (pedelecId == null) {
            return;
        }

        Pedelec pedelec = em.find(Pedelec.class, pedelecId);
        if (pedelec == null) {
            log.error("No pedelec with pedelecId: {} to update.", pedelecId);
        } else {
            setFields(pedelec, dto);
            em.merge(pedelec);
            log.debug("Updated pedelec {}", pedelec);
        }
    }

    @Override
    public void delete(long pedelecId) {
        Pedelec pedelec = em.find(Pedelec.class, pedelecId);
        if (pedelec == null) {
            log.error("No pedelec with pedelecId: {} to delete.", pedelecId);
        } else {
            em.remove(pedelec);
            log.debug("Deleted pedelec {}", pedelec);
        }

    }

    /**
    * This method sets the fields of the pedelec to the values in DTO.
    *
    * Important: The ID is not set!
    */
    private void setFields(Pedelec pedelec, CreateEditPedelecDTO dto) {
        pedelec.setState(dto.getState());
        pedelec.setManufacturerId(dto.getManufacturerId());
    }

    /**
     * This method returns the query to get information of pedelecs for various lookup cases
     *
     */
    private CriteriaQuery<ViewPedelecDTO> getQuery(CriteriaBuilder builder, Long pedelecId) {

        CriteriaQuery<ViewPedelecDTO> criteria = builder.createQuery(ViewPedelecDTO.class);
        Root<Pedelec> rootPedelec = criteria.from(Pedelec.class);
        Join<Pedelec, StationSlot> stationSlot = rootPedelec.join("stationSlot", JoinType.LEFT);
        Join<StationSlot, Station> station = stationSlot.join("station", JoinType.LEFT);

        Join<Pedelec, Transaction> trans = rootPedelec.join("transactions", JoinType.LEFT);
        Join<Transaction, Customer> transCustomerJoin = trans.join("customer", JoinType.LEFT);
        Join<Transaction, StationSlot> transStationSlotJoin = trans.join("fromSlot", JoinType.LEFT);
        Join<StationSlot, Station> transStationJoin = transStationSlotJoin.join("station", JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewPedelecDTO.class,
                        rootPedelec.get("pedelecId"),
                        rootPedelec.get("manufacturerId"),
                        rootPedelec.get("stateOfCharge"),
                        rootPedelec.get("state"),
                        rootPedelec.get("inTransaction"),
                        station.get("stationId"),
                        station.get("manufacturerId"),
                        stationSlot.get("stationSlotPosition"),
                        transCustomerJoin.get("customerId"),
                        transCustomerJoin.get("firstname"),
                        transCustomerJoin.get("lastname"),
                        transStationJoin.get("stationId"),
                        transStationSlotJoin.get("stationSlotPosition"),
                        trans.get("startDateTime")
                )
        );

        // Join with transaction table will get all the transactions for a pedelec.
        // Therefore: Only get the open transaction (toSlot & endDateTime are null)

        if (pedelecId == null) {
            criteria.where(
                    builder.and(
                            builder.isNull(trans.get("toSlot")),
                            builder.isNull(trans.get("endDateTime"))
                    )
            );
        } else {
            criteria.where(
                    builder.and(
                            builder.equal(rootPedelec.get("pedelecId"), pedelecId),
                            builder.isNull(trans.get("toSlot")),
                            builder.isNull(trans.get("endDateTime"))
                    )
            );
        }

        return criteria;
    }
}

