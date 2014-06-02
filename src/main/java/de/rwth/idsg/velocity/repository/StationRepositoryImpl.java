package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Address;
import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.domain.StationSlot;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationSlotDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sgokay on 26.05.14.
 */
@Repository
@Transactional
public class StationRepositoryImpl implements StationRepository {

    private static final Logger log = LoggerFactory.getLogger(StationRepositoryImpl.class);

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ViewStationDTO> findAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ViewStationDTO> criteria = this.getStationQuery(builder, null);
        return em.createQuery(criteria).getResultList();
    }

    @Override
    public List<ViewStationDTO> findByLocation(BigDecimal latitude, BigDecimal longitude) {
        // TODO
        return null;
    }

    @Override
    public ViewStationDTO findOne(long stationId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        // get station info
        CriteriaQuery<ViewStationDTO> criteria = this.getStationQuery(builder, stationId);
        ViewStationDTO stat = em.createQuery(criteria).getSingleResult();

        // get slots for the station
        CriteriaQuery<ViewStationSlotDTO> slotCriteria = builder.createQuery(ViewStationSlotDTO.class);
        Root<StationSlot> rootSlot = slotCriteria.from(StationSlot.class);
        Join<StationSlot, Pedelec> pedelecJoin = rootSlot.join("pedelec", JoinType.LEFT);

        slotCriteria.select(
                builder.construct(
                        ViewStationSlotDTO.class,
                        rootSlot.get("stationSlotId"),
                        rootSlot.get("manufacturerId"),
                        rootSlot.get("stationSlotPosition"),
                        rootSlot.get("state"),
                        rootSlot.get("isOccupied"),
                        pedelecJoin.get("pedelecId"),
                        pedelecJoin.get("manufacturerId")
                )
        ).where(builder.equal(rootSlot.get("station").get("stationId"), stationId));

        List<ViewStationSlotDTO> list = em.createQuery(slotCriteria).getResultList();
        stat.setSlots(list);

        return stat;
    }

    @Override
    public void create(CreateEditStationDTO dto) {
        Station station = new Station();
        setFields(station, dto);
        em.persist(station);
        log.debug("Created new station {}", station);
    }

    @Override
    public void update(CreateEditStationDTO dto) {
        final Long stationId = dto.getStationId();
        if (stationId == null) {
            return;
        }

        Station station = em.find(Station.class, stationId);
        if (station == null) {
            log.error("No station with stationId: {} to update.", stationId);
        } else {
            setFields(station, dto);
            em.merge(station);
            log.debug("Updated station {}", station);
        }
    }

    @Override
    public void delete(long stationId) {
        Station station = em.find(Station.class, stationId);
        if (station == null) {
            log.error("No station with stationId: {} to delete.", stationId);
        } else {
            em.remove(station);
            log.debug("Deleted station {}", station);
        }
    }

    /*
    * This method sets the fields of the station to the values in DTO.
    *
    * Important: The ID is not set!
    */
    private void setFields(Station station, CreateEditStationDTO dto) {
        station.setManufacturerId(dto.getManufacturerId());
        station.setName(dto.getName());
        station.setAddress(dto.getAddress());
        station.setLocationLatitude(dto.getLocationLatitude());
        station.setLocationLongitude(dto.getLocationLongitude());
        station.setNote(dto.getNote());
        station.setState(dto.getState());
    }

    /*
    * This method returns the query to get information of all the stations or only the specified station (by stationId)
    *
    */
    private CriteriaQuery<ViewStationDTO> getStationQuery(CriteriaBuilder builder, Long stationId) {
        CriteriaQuery<ViewStationDTO> criteria = builder.createQuery(ViewStationDTO.class);

        Root<Station> root = criteria.from(Station.class);
        Join<Station, StationSlot> slot = root.join("stationSlots");
        Join<Station, Address> add = root.join("address");
        Path<Boolean> occ = slot.get("isOccupied");

        criteria.select(
                builder.construct(
                        ViewStationDTO.class,
                        root.get("stationId"),
                        root.get("manufacturerId"),
                        root.get("name"),
                        add.get("streetAndHousenumber"),
                        add.get("zip"),
                        add.get("city"),
                        add.get("country"),
                        root.get("locationLatitude"),
                        root.get("locationLongitude"),
                        root.get("note"),
                        root.get("state"),
                        builder.sum(builder.<Integer>selectCase()
                                        .when(builder.isFalse(occ), 1)
                                        .otherwise(0)
                        ),
                        builder.count(occ)
                )
        ).groupBy(root.get("stationId"), add.get("addressId"));

        if (stationId != null) {
            criteria.where(builder.equal(root.get("stationId"), stationId));
        }

        return criteria;
    }

}
