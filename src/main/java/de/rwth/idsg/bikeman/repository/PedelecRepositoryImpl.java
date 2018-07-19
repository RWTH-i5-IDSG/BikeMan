package de.rwth.idsg.bikeman.repository;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.domain.CustomerType;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewErrorDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
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

        List<ViewPedelecDTO> list = findPedelecsInTransaction(builder, CustomerType.CUSTOMER);
        list.addAll(findPedelecsInTransaction(builder, CustomerType.MAJOR_CUSTOMER));
        list.addAll(findStationaryPedelecs(builder));
        return list;
    }

    @SuppressWarnings("unchecked")
    private List<ViewPedelecDTO> findPedelecsInTransaction(CriteriaBuilder builder, CustomerType customerType) {
        CriteriaQuery<ViewPedelecDTO> criteria = builder.createQuery(ViewPedelecDTO.class);

        Root<Pedelec> pedelec = criteria.from(Pedelec.class);
        Join<Pedelec, Transaction> transaction = pedelec.join(Pedelec_.transactions, JoinType.LEFT);

        Join<Transaction, CardAccount> cardAccount = transaction.join(Transaction_.cardAccount, JoinType.LEFT);
        Join user = cardAccount.join(CardAccount_.user, JoinType.LEFT);

        Join<Transaction, StationSlot> fromStationSlot = transaction.join(Transaction_.fromSlot, JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromStationSlot.join(StationSlot_.station, JoinType.LEFT);

        Join<Pedelec, PedelecChargingStatus> chargingStatus = pedelec.join(Pedelec_.chargingStatus, JoinType.LEFT);

        switch (customerType) {
            case CUSTOMER:
                criteria.select(
                        builder.construct(
                                ViewPedelecDTO.class,
                                pedelec.get(Pedelec_.pedelecId),
                                pedelec.get(Pedelec_.manufacturerId),
                                chargingStatus.get(PedelecChargingStatus_.batteryStateOfCharge),
                                pedelec.get(Pedelec_.state),
                                pedelec.get(Pedelec_.inTransaction),
                                cardAccount.get(CardAccount_.cardId),
                                user.get(Customer_.customerId),
                                user.get(Customer_.firstname),
                                user.get(Customer_.lastname),
                                fromStation.get(Station_.stationId),
                                fromStationSlot.get(StationSlot_.stationSlotPosition),
                                transaction.get(Transaction_.startDateTime)
                        )
                );
                break;

            case MAJOR_CUSTOMER:
                criteria.select(
                        builder.construct(
                                ViewPedelecDTO.class,
                                pedelec.get(Pedelec_.pedelecId),
                                pedelec.get(Pedelec_.manufacturerId),
                                chargingStatus.get(PedelecChargingStatus_.batteryStateOfCharge),
                                pedelec.get(Pedelec_.state),
                                pedelec.get(Pedelec_.inTransaction),
                                cardAccount.get(CardAccount_.cardId),
                                user.get(MajorCustomer_.name),
                                fromStation.get(Station_.stationId),
                                fromStationSlot.get(StationSlot_.stationSlotPosition),
                                transaction.get(Transaction_.startDateTime)
                        )
                );
                break;
        }

        criteria.where(
                builder.and(
                        builder.equal(cardAccount.get(CardAccount_.ownerType), customerType),
                        builder.equal(pedelec.get(Pedelec_.inTransaction), true),
                        builder.isNull(transaction.get(Transaction_.endDateTime)),
                        builder.isNull(transaction.get(Transaction_.toSlot))
                ));

        return em.createQuery(criteria).getResultList();
    }

    private List<ViewPedelecDTO> findStationaryPedelecs(CriteriaBuilder builder) {
        CriteriaQuery<ViewPedelecDTO> criteria = builder.createQuery(ViewPedelecDTO.class);

        Root<Pedelec> pedelec = criteria.from(Pedelec.class);
        Join<Pedelec, StationSlot> stationSlot = pedelec.join(Pedelec_.stationSlot, JoinType.LEFT);
        Join<StationSlot, Station> station = stationSlot.join(StationSlot_.station, JoinType.LEFT);

        Join<Pedelec, PedelecChargingStatus> chargingStatus = pedelec.join(Pedelec_.chargingStatus, JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewPedelecDTO.class,
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId),
                        chargingStatus.get(PedelecChargingStatus_.batteryStateOfCharge),
                        pedelec.get(Pedelec_.state),
                        pedelec.get(Pedelec_.inTransaction),
                        station.get(Station_.stationId),
                        station.get(Station_.name),
                        stationSlot.get(StationSlot_.stationSlotPosition),
                        chargingStatus.get(PedelecChargingStatus_.timestamp),
                        chargingStatus.get(PedelecChargingStatus_.state)
                )
        ).where(builder.equal(pedelec.get(Pedelec_.inTransaction), false));

        return em.createQuery(criteria).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public ViewPedelecDTO findOneDTO(Long pedelecId) throws DatabaseException {

        final String q = "SELECT new de.rwth.idsg.bikeman.web.rest.dto.view." +
                "ViewPedelecDTO(p.pedelecId, p.manufacturerId, cs.batteryStateOfCharge, p.state, p.inTransaction) " +
                "FROM Pedelec p " +
                "LEFT JOIN p.chargingStatus cs " +
                "WHERE p.pedelecId = :pedelecId";

        try {
            return em.createQuery(q, ViewPedelecDTO.class)
                     .setParameter("pedelecId", pedelecId)
                     .getSingleResult();
        } catch (Exception e) {
            throw new DatabaseException("Failed to find pedelec with pedelecId " + pedelecId, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Pedelec findOne(long pedelecId) throws DatabaseException {
        return getPedelecEntity(pedelecId);
    }

    @Transactional(readOnly = true)
    public Pedelec findByManufacturerId(String manufacturerId) throws DatabaseException {
        final String q = "SELECT p FROM Pedelec p WHERE p.manufacturerId = :manufacturerId";

        try {
            return em.createQuery(q, Pedelec.class)
                     .setParameter("manufacturerId", manufacturerId)
                     .getSingleResult();
        } catch (Exception e) {
            throw new DatabaseException("Failed to find pedelec with manufacturerId " + manufacturerId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Pedelec> findByStation(String stationManufacturerId) throws DatabaseException {

        final String q = "select p from Pedelec p where p.stationSlot.station.manufacturerId = :stationManufacturerId";

        try {
            return em.createQuery(q, Pedelec.class)
                     .setParameter("stationManufacturerId", stationManufacturerId)
                     .getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to find pedelecs by stationManufacturerId " + stationManufacturerId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> findManufacturerIdsByStation(String stationManufacturerId) throws DatabaseException {

        final String q = "select p.manufacturerId from Pedelec p where p.stationSlot.station.manufacturerId = :stationManufacturerId";

        try {
            return em.createQuery(q, String.class)
                     .setParameter("stationManufacturerId", stationManufacturerId)
                     .getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to find pedelecs by stationManufacturerId " + stationManufacturerId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pedelec findByStationSlot(String stationSlotManufacturerId) throws DatabaseException {

        final String q = "SELECT p FROM Pedelec p WHERE p.stationSlot.manufacturerId = :manufacturerId";

        try {
            return em.createQuery(q, Pedelec.class)
                     .setParameter("manufacturerId", stationSlotManufacturerId)
                     .getSingleResult();
        } catch (Exception e) {
            throw new DatabaseException("Failed to find pedelec with stationSlotManufacturerId " + stationSlotManufacturerId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<Pedelec> findPedelecsByStationSlot(String stationManufacturerId, String stationSlotManufacturerId)
            throws DatabaseException {

        final String q = "SELECT p FROM Pedelec p " +
                         "WHERE p.stationSlot.manufacturerId = :stationSlotManufacturerId " +
                         "AND p.stationSlot.station.manufacturerId = :stationManufacturerId";

        try {
            Pedelec p = em.createQuery(q, Pedelec.class)
                          .setParameter("stationSlotManufacturerId", stationSlotManufacturerId)
                          .setParameter("stationManufacturerId", stationManufacturerId)
                          .getSingleResult();

            return Optional.of(p);

        } catch (NoResultException e) {
            // Do nothing, this is a valid outcome
        } catch (Exception e) {
            log.error("Error occurred", e);
        }

        return Optional.absent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewErrorDTO> findErrors() throws DatabaseException {
        String q = "SELECT new de.rwth.idsg.bikeman.web.rest.dto.view.ViewErrorDTO" +
                "(p.pedelecId, p.manufacturerId, p.errorCode, p.errorInfo, p.updated) " +
                "FROM Pedelec p where not (p.errorCode = '') and p.errorCode is not null";

        try {
            return em.createQuery(q, ViewErrorDTO.class)
                    .getResultList();
        } catch (Exception e) {
            log.error("Error occurred", e);
            throw new DatabaseException("PedelecRepository exception while looking for errors");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateEditPedelecDTO dto) throws DatabaseException {
        Pedelec pedelec = new Pedelec();
        pedelec.setManufacturerId(dto.getManufacturerId());
        pedelec.setState(dto.getState());

        try {
            em.persist(pedelec);
            conditionalChargingStatusInsert(pedelec);
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
        pedelec.setState(dto.getState());

        try {
            em.merge(pedelec);
            conditionalChargingStatusInsert(pedelec);
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

    private void conditionalChargingStatusInsert(Pedelec pedelec) {
        if (pedelec.getChargingStatus() == null) {
            PedelecChargingStatus status = new PedelecChargingStatus();
            status.setPedelec(pedelec);
            pedelec.setChargingStatus(status);
            em.persist(status);
        }
    }
}
