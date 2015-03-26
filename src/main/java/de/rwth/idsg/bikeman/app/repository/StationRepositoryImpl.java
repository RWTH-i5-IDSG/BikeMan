package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.ViewStationDTO;
import de.rwth.idsg.bikeman.app.dto.ViewStationSlotsDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

@Repository("StationRepositoryImplApp")
@Slf4j
public class StationRepositoryImpl implements StationRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<ViewStationDTO> findAll() throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ViewStationDTO> criteria = this.getStationQuery(builder, null);

        try {
            return em.createQuery(criteria).getResultList();

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ViewStationDTO findOne(long stationId) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ViewStationDTO> criteria = this.getStationQuery(builder, stationId);

        try {
            return em.createQuery(criteria).getSingleResult();

        } catch (NoResultException e) {
            throw new AppException("Failed to find station with stationId " + stationId, e, AppErrorCode.CONSTRAINT_FAILED);
        } catch (Exception e) {
            throw new AppException("Failed during database operation", e, AppErrorCode.DATABASE_OPERATION_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStationSlotsDTO> findOneWithSlots(long stationId) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<ViewStationSlotsDTO> criteria = builder.createQuery(ViewStationSlotsDTO.class);

        Root<StationSlot> stationSlot = criteria.from(StationSlot.class);
        Join<StationSlot, Pedelec> pedelec = stationSlot.join(StationSlot_.pedelec, JoinType.LEFT);

        criteria.select(
                builder.construct(
                        ViewStationSlotsDTO.class,
                        stationSlot.get(StationSlot_.stationSlotId),
                        stationSlot.get(StationSlot_.stationSlotPosition),
                        stationSlot.get(StationSlot_.state),
                        stationSlot.get(StationSlot_.isOccupied),
                        pedelec.get(Pedelec_.stateOfCharge)
                )
        ).where(
                builder.equal(stationSlot.get(StationSlot_.station).get(Station_.stationId), stationId)
        ).orderBy(
                builder.asc(stationSlot.get(StationSlot_.stationSlotPosition))
        );

        try {
            List<ViewStationSlotsDTO> slots = em.createQuery(criteria).getResultList();

            if (slots.isEmpty()) {
                throw new NoResultException();
            }

            return slots;

        } catch (NoResultException e) {
            throw new AppException("Failed to find station with stationId " + stationId, e, AppErrorCode.CONSTRAINT_FAILED);
        } catch (Exception e) {
            throw new AppException("Failed during database operation", e, AppErrorCode.DATABASE_OPERATION_FAILED);
        }
    }


    private CriteriaQuery<ViewStationDTO> getStationQuery(CriteriaBuilder builder, Long stationId) {
        CriteriaQuery<ViewStationDTO> criteria = builder.createQuery(ViewStationDTO.class);

        Root<Station> station = criteria.from(Station.class);
        Join<Station, StationSlot> stationSlot = station.join(Station_.stationSlots, JoinType.LEFT);

        Path<Boolean> occ = stationSlot.get(StationSlot_.isOccupied);

        criteria.select(
                builder.construct(
                        ViewStationDTO.class,
                        station.get(Station_.stationId),
                        station.get(Station_.name),
                        station.get(Station_.locationLatitude),
                        station.get(Station_.locationLongitude),
                        station.get(Station_.state),
                        station.get(Station_.note),
                        builder.sum(builder.<Integer>selectCase()
                                        .when(builder.isFalse(occ), 1)
                                        .otherwise(0)
                        ),
                        builder.count(occ)
                )
        );

        if (stationId != null) {
            criteria.where(builder.equal(station.get(Station_.stationId), stationId));
        }

        criteria.groupBy(station.get(Station_.stationId));

        return criteria;
    }
}
