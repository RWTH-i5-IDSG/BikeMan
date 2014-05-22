package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewPedelecDTO;
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
public class PedelecRepositoryImpl implements PedelecRepository{

    @PersistenceContext
    EntityManager em;

    @Override
    public List<Pedelec> findAll() {
        return em.createQuery("SELECT ped FROM Pedelec ped", Pedelec.class)
                .getResultList();
    }

    @Override
    public Pedelec findOne(Long id) {
        return em.createQuery("SELECT ped FROM Pedelec ped WHERE ped.id = :id", Pedelec.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void save(Pedelec pedelec) {
        em.persist(pedelec);
    }

    @Override
    public void delete(Long id) {
        em.createQuery("DELETE FROM Pedelec ped WHERE ped.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<ViewPedelecDTO> viewPedelecs() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ViewPedelecDTO> criteria = builder.createQuery(ViewPedelecDTO.class);
        Root<Pedelec> root = criteria.from(Pedelec.class);

        criteria.select(
                builder.construct(
                        ViewPedelecDTO.class,
                        root.get("pedelecId"),
                        root.get("manufacturerId"),
                        root.get("stateOfCharge"),
                        root.get("state"),
                        root.get("stationSlot").get("stationSlotId"),
                        root.get("stationSlot").get("station").get("stationId"),
                        root.get("stationSlot").get("state")
                )
        );

        return em.createQuery(criteria).getResultList();

    }
}

