package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.domain.CardAccount_;
import de.rwth.idsg.bikeman.domain.Customer_;
import de.rwth.idsg.bikeman.domain.MajorCustomer_;
import de.rwth.idsg.bikeman.domain.Pedelec_;
import de.rwth.idsg.bikeman.domain.StationSlot_;
import de.rwth.idsg.bikeman.domain.Station_;
import de.rwth.idsg.bikeman.domain.Transaction_;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.ChargingStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.PedelecStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.AvailablePedelecDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.Date;
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

        List<ViewPedelecDTO> list = findPedelecsInTransactionWithCustomer(builder);
        list.addAll(findPedelecsInTransactionWithMajorCustomer(builder));
        list.addAll(findStationaryPedelecs(builder));
        return list;
    }

    // nachfolgende funktion nur mit einem rückgabewert (keine liste) und als parameter stationId statt endpointAddress

    @Override
    @Transactional(readOnly = true)
    public List<AvailablePedelecDTO> findAvailablePedelecs(String endpointAddress) throws DatabaseException {
        final String q = "SELECT new de.rwth.idsg.bikeman.psinterface.dto.response." +
                         "AvailablePedelecDTO(p.manufacturerId) " +
                         "from Pedelec p " +
                         "where p.stationSlot.station.endpointAddress = :endpointAddress " +
                         "and p.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
                         "order by p.stateOfCharge desc";

        try {
            return em.createQuery(q, AvailablePedelecDTO.class)
                    .setParameter("endpointAddress", endpointAddress)
                    .setMaxResults(5)
                    .getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to find pedelecs in station with endpoint address" + endpointAddress, e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<ViewPedelecDTO> findPedelecsInTransactionWithCustomer(CriteriaBuilder builder) {
        CriteriaQuery<ViewPedelecDTO> criteria = builder.createQuery(ViewPedelecDTO.class);

        Root<Pedelec> pedelec = criteria.from(Pedelec.class);
        Join<Pedelec, Transaction> transaction = pedelec.join(Pedelec_.transactions, JoinType.LEFT);

        Join<Transaction, CardAccount> cardAccount = transaction.join(Transaction_.cardAccount, JoinType.LEFT);
        Join customer = cardAccount.join(CardAccount_.user, JoinType.LEFT);

        Join<Transaction, StationSlot> fromStationSlot = transaction.join(Transaction_.fromSlot, JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromStationSlot.join(StationSlot_.station, JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewPedelecDTO.class,
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId),
                        pedelec.get(Pedelec_.stateOfCharge),
                        pedelec.get(Pedelec_.state),
                        pedelec.get(Pedelec_.inTransaction),
                        cardAccount.get(CardAccount_.cardId),
                        customer.get(Customer_.customerId),
                        customer.get(Customer_.firstname),
                        customer.get(Customer_.lastname),
                        fromStation.get(Station_.stationId),
                        fromStationSlot.get(StationSlot_.stationSlotPosition),
                        transaction.get(Transaction_.startDateTime)
                )
        ).where(builder.and(
                builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.CUSTOMER),
                builder.equal(pedelec.get(Pedelec_.inTransaction), true),
                builder.isNull(transaction.get(Transaction_.endDateTime)),
                builder.isNull(transaction.get(Transaction_.toSlot))
        ));

        return em.createQuery(criteria).getResultList();
    }

    @SuppressWarnings("unchecked")
    private List<ViewPedelecDTO> findPedelecsInTransactionWithMajorCustomer(CriteriaBuilder builder) {
        CriteriaQuery<ViewPedelecDTO> criteria = builder.createQuery(ViewPedelecDTO.class);

        Root<Pedelec> pedelec = criteria.from(Pedelec.class);
        Join<Pedelec, Transaction> transaction = pedelec.join(Pedelec_.transactions, JoinType.LEFT);
        Join<Transaction, CardAccount> cardAccount = transaction.join(Transaction_.cardAccount, JoinType.LEFT);
        Join majorCustomer = cardAccount.join(CardAccount_.user, JoinType.LEFT);

        Join<Transaction, StationSlot> fromStationSlot = transaction.join(Transaction_.fromSlot, JoinType.LEFT);
        Join<StationSlot, Station> fromStation = fromStationSlot.join(StationSlot_.station, JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewPedelecDTO.class,
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId),
                        pedelec.get(Pedelec_.stateOfCharge),
                        pedelec.get(Pedelec_.state),
                        pedelec.get(Pedelec_.inTransaction),
                        cardAccount.get(CardAccount_.cardId),
                        majorCustomer.get(MajorCustomer_.name),
                        fromStation.get(Station_.stationId),
                        fromStationSlot.get(StationSlot_.stationSlotPosition),
                        transaction.get(Transaction_.startDateTime)
                )
        ).where(builder.and(
                builder.equal(cardAccount.get(CardAccount_.ownerType), CustomerType.MAJOR_CUSTOMER),
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

        criteria.select(
                builder.construct(
                        ViewPedelecDTO.class,
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId),
                        pedelec.get(Pedelec_.stateOfCharge),
                        pedelec.get(Pedelec_.state),
                        pedelec.get(Pedelec_.inTransaction),
                        station.get(Station_.stationId),
                        station.get(Station_.manufacturerId),
                        stationSlot.get(StationSlot_.stationSlotPosition)
                )
        ).where(builder.equal(pedelec.get(Pedelec_.inTransaction), false));

        return em.createQuery(criteria).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public ViewPedelecDTO findOneDTO(Long pedelecId) throws DatabaseException {

        final String q = "SELECT new de.rwth.idsg.bikeman.web.rest.dto.view." +
                         "ViewPedelecDTO(p.pedelecId, p.manufacturerId, p.stateOfCharge, p.state, p.inTransaction) " +
                         "FROM Pedelec p " +
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePedelecStatus(PedelecStatusDTO dto) {
        final String s = "UPDATE Pedelec p SET " +
                         "p.errorCode = :pedelecErrorCode, " +
                         "p.errorInfo = :pedelecErrorInfo, " +
                         "p.state = :pedelecState, " +
                         "p.updated = :updated " +
                         "WHERE p.manufacturerId = :pedelecManufacturerId";

        try {
            em.createQuery(s)
              .setParameter("pedelecErrorCode", dto.getPedelecErrorCode())
              .setParameter("pedelecErrorInfo", dto.getPedelecErrorInfo())
              .setParameter("pedelecState", OperationState.valueOf(dto.getPedelecState().name()))
              .setParameter("updated", new Date(Utils.toMillis(dto.getTimestamp())))
              .setParameter("pedelecManufacturerId", dto.getPedelecManufacturerId())
              .executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException("Failed to update the pedelec status with manufacturerId " + dto.getPedelecManufacturerId(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePedelecChargingStatus(List<ChargingStatusDTO> dtoList) {
        final String s = "UPDATE Pedelec p SET " +
                "p.stateOfCharge = :stateOfCharge " +
                "WHERE p.manufacturerId = :pedelecManufacturerId";

        //TODO: currently only SOC gets updated

        try {
            for (ChargingStatusDTO dto : dtoList) {
                em.createQuery(s)
                        .setParameter("stateOfCharge", new Float(dto.getBattery().getSoc()))
                        .setParameter("pedelecManufacturerId", dto.getPedelecManufacturerId())
                        .executeUpdate();
            }
        } catch (Exception e) {
            throw new DatabaseException("Failed to update the charging status.", e);
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
