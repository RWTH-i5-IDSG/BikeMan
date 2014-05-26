package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.domain.Transaction;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewPedelecDTO;
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
        criteriaStat.select(
                builder.construct(
                        ViewPedelecDTO.class,
                        rootStat.get("pedelecId"),
                        rootStat.get("manufacturerId"),
                        rootStat.get("stateOfCharge"),
                        rootStat.get("state"),
                        rootStat.get("inTransaction"),
                        rootStat.get("stationSlot").get("station").get("stationId"),
                        rootStat.get("stationSlot").get("station").get("manufacturerId"),
                        rootStat.get("stationSlot").get("stationSlotId")
                )
        );
        criteriaStat.where(builder.equal(rootStat.get("inTransaction"), false));
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
                        trans.get("fromSlot").get("stationSlotId"),
                        trans.get("startDateTime")
                )
        );
        criteriaTrans.where(builder.equal(rootTrans.get("inTransaction"), true));

        // Join the lists and return
        list.addAll(em.createQuery(criteriaTrans).getResultList());
        return list;
    }

    @Override
    public Pedelec findOne(Long pedelecId) {
        return em.createQuery("SELECT ped FROM Pedelec ped WHERE ped.pedelecId = :pedelecId", Pedelec.class)
                .setParameter("pedelecId", pedelecId)
                .getSingleResult();
    }

    @Override
    public void create(CreateEditPedelecDTO dto) {
        Pedelec pedelec = new Pedelec();
        pedelec.setState(dto.getState());
        pedelec.setStateOfCharge(0.0f);
        pedelec.setManufacturerId(dto.getManufacturerId());

        em.persist(pedelec);

        log.debug("Created new Pedelec {}", pedelec);
    }

    @Override
    public void update(CreateEditPedelecDTO dto) {
        int rowCount = em.createQuery("UPDATE Pedelec ped SET ped.manufacturerId = :manufacturerId, ped.state = :state WHERE ped.pedelecId = :pedelecId")
                .setParameter("manufacturerId", dto.getManufacturerId())
                .setParameter("state", dto.getState())
                .setParameter("pedelecId", dto.getPedelecId())
                .executeUpdate();

        logChanges(rowCount);
    }

    @Override
    public void delete(Long pedelecId) {
        int rowCount = em.createQuery("DELETE FROM Pedelec ped WHERE ped.pedelecId = :pedelecId")
                .setParameter("pedelecId", pedelecId)
                .executeUpdate();

        logChanges(rowCount);
    }

    private void logChanges(int rowCount) {
        if (rowCount == 1) {
            log.debug("Affected rows: {}.", rowCount);
        } else {
            log.error("Affected rows: {}. Something is wrong.", rowCount);
        }
    }
}

