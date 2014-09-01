package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.*;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewPedelecDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by sgokay on 21.05.14.
 */
@Repository
@Slf4j
public class PedelecRepositoryImpl implements PedelecRepository {

    @PersistenceContext private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<ViewPedelecDTO> findAll() throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Get the pedelecs in transaction
        CriteriaQuery<ViewPedelecDTO> criteriaTrans = builder.createQuery(ViewPedelecDTO.class);
        Root<Pedelec> rootTrans = criteriaTrans.from(Pedelec.class);
        Join<Pedelec, Transaction> trans = rootTrans.join("transactions", JoinType.LEFT);
        Join<Transaction, Customer> transCustomerJoin = trans.join("customer", JoinType.LEFT);
        Join<Transaction, StationSlot> transStationSlotJoin = trans.join("fromSlot", JoinType.LEFT);
        Join<StationSlot, Station> transStationJoin = transStationSlotJoin.join("station", JoinType.LEFT);

        criteriaTrans.select(
                builder.construct(
                        ViewPedelecDTO.class,
                        rootTrans.get("pedelecId"),
                        rootTrans.get("manufacturerId"),
                        rootTrans.get("stateOfCharge"),
                        rootTrans.get("state"),
                        rootTrans.get("inTransaction"),
                        transCustomerJoin.get("customerId"),
                        transCustomerJoin.get("firstname"),
                        transCustomerJoin.get("lastname"),
                        transStationJoin.get("stationId"),
                        transStationSlotJoin.get("stationSlotPosition"),
                        trans.get("startDateTime")
                )
        ).where(builder.and(
                builder.equal(rootTrans.get("inTransaction"), true),
                builder.isNull(trans.get("endDateTime")),
                builder.isNull(trans.get("toSlot"))
        ));

        List<ViewPedelecDTO> list = em.createQuery(criteriaTrans).getResultList();

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

        // Join the lists and return
        list.addAll(em.createQuery(criteriaStat).getResultList());
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public ViewPedelecDTO findOneDTO(Long pedelecId) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        try {
            // Get the pedelecs in transaction
            CriteriaQuery<ViewPedelecDTO> criteria = builder.createQuery(ViewPedelecDTO.class);
            Root<Pedelec> root = criteria.from(Pedelec.class);

            criteria.select(
                    builder.construct(
                            ViewPedelecDTO.class,
                            root.get("pedelecId"),
                            root.get("manufacturerId"),
                            root.get("stateOfCharge"),
                            root.get("state"),
                            root.get("inTransaction")
                    )
            ).where(builder.equal(root.get("pedelecId"), pedelecId));

            return em.createQuery(criteria).getSingleResult();

        } catch (Exception e) {
            throw new DatabaseException("Failed to find pedelec with pedelecId " + pedelecId, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Pedelec findOne(long pedelecId) throws DatabaseException {
        return getPedelecEntity(pedelecId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateEditPedelecDTO dto) throws DatabaseException {
        Pedelec pedelec = new Pedelec();
        setFields(pedelec, dto);
        try {
            em.persist(pedelec);
            log.debug("Created new pedelec {}", pedelec);

        } catch (EntityExistsException e) {
            throw new DatabaseException("This pedelec exists already.", e);

        } catch (Exception e) {
            throw new DatabaseException("Failed to create a new pedelec.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CreateEditPedelecDTO dto) throws DatabaseException {
        final Long pedelecId = dto.getPedelecId();
        if (pedelecId == null) {
            return;
        }

        Pedelec pedelec = getPedelecEntity(pedelecId);
        setFields(pedelec, dto);

        try {
            em.merge(pedelec);
            log.debug("Updated pedelec {}", pedelec);

        } catch (Exception e) {
            throw new DatabaseException("Failed to update pedelec with pedelecId " + pedelecId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long pedelecId) throws DatabaseException {
        Pedelec pedelec = getPedelecEntity(pedelecId);

        try {
            em.remove(pedelec);
            log.debug("Deleted pedelec {}", pedelec);

        } catch (Exception e) {
            throw new DatabaseException("Failed to delete pedelec with pedelec " + pedelecId, e);
        }
    }

    /**
     * Returns a pedelec, or throws exception when no pedelec exists.
     *
     */
    @Transactional(readOnly = true)
    private Pedelec getPedelecEntity(long pedelecId) throws DatabaseException {
        Pedelec pedelec = em.find(Pedelec.class, pedelecId);
        if (pedelec == null) {
            throw new DatabaseException("No pedelec with pedelecId " + pedelecId);
        } else {
            return pedelec;
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
}

