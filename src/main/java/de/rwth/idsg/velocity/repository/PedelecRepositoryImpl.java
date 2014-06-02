package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.domain.StationSlot;
import de.rwth.idsg.velocity.domain.Transaction;
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

        // Get the stationary pedelecs
        CriteriaQuery<ViewPedelecDTO> criteriaStat = builder.createQuery(ViewPedelecDTO.class);
        Root<Pedelec> rootStat = criteriaStat.from(Pedelec.class);
        Join<Pedelec, StationSlot> stationSlot = rootStat.join("stationSlot", JoinType.LEFT);
        Join<Pedelec, Station> station = stationSlot.join("station", JoinType.LEFT);

        criteriaStat.select(
                builder.construct(
                        ViewPedelecDTO.class,
                        rootStat.get("pedelecId"),
                        rootStat.get("manufacturerId"),
                        rootStat.get("stateOfCharge"),
                        rootStat.get("state"),
                        rootStat.get("inTransaction"),
                        station.get("stationId"),
                        station.get("manufacturerId"),
                        stationSlot.get("stationSlotPosition")
                )
        ).where(builder.equal(rootStat.get("inTransaction"), false));

        List<ViewPedelecDTO> list = em.createQuery(criteriaStat).getResultList();

        // Get the pedelecs in transaction
        CriteriaQuery<ViewPedelecDTO> criteriaTrans = builder.createQuery(ViewPedelecDTO.class);
        Root<Pedelec> rootTrans = criteriaTrans.from(Pedelec.class);
        Root<Transaction> trans = criteriaTrans.from(Transaction.class);
        criteriaTrans.select(
                builder.construct(
                        ViewPedelecDTO.class,
                        rootTrans.get("pedelecId"),
                        rootTrans.get("manufacturerId"),
                        rootTrans.get("stateOfCharge"),
                        rootTrans.get("state"),
                        rootTrans.get("inTransaction"),
                        trans.get("customer").get("customerId"),
                        trans.get("customer").get("firstname"),
                        trans.get("customer").get("lastname"),
                        trans.get("fromSlot").get("station").get("stationId"),
                        trans.get("fromSlot").get("stationSlotPosition"),
                        trans.get("startDateTime")
                )
        ).where(builder.and(
                builder.equal(rootTrans.get("inTransaction"), true),
                builder.isNull(trans.get("endDateTime"))
        ));

        // Join the lists and return
        list.addAll(em.createQuery(criteriaTrans).getResultList());
        return list;
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

    /*
    * This method sets the fields of the pedelec to the values in DTO.
    *
    * Important: The ID is not set!
    */
    private void setFields(Pedelec pedelec, CreateEditPedelecDTO dto) {
        pedelec.setState(dto.getState());
        pedelec.setManufacturerId(dto.getManufacturerId());
    }
}

