package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.*;
import de.rwth.idsg.velocity.web.rest.BackendException;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationSlotDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityExistsException;
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

    private enum Operation { CREATE, UPDATE };

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ViewStationDTO> findAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ViewStationDTO> criteria = this.getStationQuery(builder, null);
        return em.createQuery(criteria).getResultList();
    }

    @Override
    public List<ViewStationDTO> findByLocation(BigDecimal latitude, BigDecimal longitude)  throws BackendException {
        // TODO

        return null;
    }

    @Override
    public ViewStationDTO findOne(long stationId) throws BackendException {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        // get station info

        CriteriaQuery<ViewStationDTO> criteria = this.getStationQuery(builder, stationId);

        ViewStationDTO stat = null;

        try {
            stat = em.createQuery(criteria).getSingleResult();
        } catch (Exception e) {
            throw new BackendException("Failed to find station.");
        }

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

        try {
            List<ViewStationSlotDTO> list = em.createQuery(slotCriteria).getResultList();
            stat.setSlots(list);
        } catch (Exception e) {
            throw new BackendException("Failted to get slots for the station");
        }

        return stat;
    }

    @Override
    public void create(CreateEditStationDTO dto) throws BackendException {
        Station station = new Station();
        setFields(station, dto, Operation.CREATE);

        try {
            em.persist(station);
            log.debug("Created new manager {}", station);

        } catch (EntityExistsException e) {
            throw new BackendException("This station exists already.");

        } catch (Exception e) {
            throw new BackendException("Failed to create a new station.");
        }
    }

    @Override
    public void update(CreateEditStationDTO dto) throws BackendException {
        final Long stationId = dto.getStationId();
        if (stationId == null) {
            return;
        }

        Station station = getStationEntity(stationId);
        setFields(station,dto, Operation.UPDATE);

        try {
            em.merge(station);
            log.debug("Updated station {}", station);

        } catch (Exception e) {
            throw new BackendException("Failed to update station with stationId " + stationId);
        }
    }

    @Override
    public void delete(long stationId) throws BackendException {
        Station station = getStationEntity(stationId);
        try {
            em.remove(station);
            log.debug("Deleted station {}", station);
        } catch (Exception e) {
            throw new BackendException("Failed to delete station with stationId " + stationId);
        }
    }

    /**
     * Returns a station, or throws exception when no station exists.
     *
     */
    private Station getStationEntity(long stationId) throws BackendException {
        Station station = em.find(Station.class, stationId);
        if (station == null) {
            throw new BackendException("No station with stationId " + stationId);
        } else {
            return station;
        }
    }

    /**
    * This method sets the fields of the station to the values in DTO.
    *
    * Important: The ID is not set!
    */
    private void setFields(Station station, CreateEditStationDTO dto, Operation operation) {
        station.setManufacturerId(dto.getManufacturerId());
        station.setName(dto.getName());
        station.setLocationLatitude(dto.getLocationLatitude());
        station.setLocationLongitude(dto.getLocationLongitude());
        station.setNote(dto.getNote());
        station.setState(dto.getState());

        switch (operation) {
            case CREATE:
                // for create (brand new address entity)
                station.setAddress(dto.getAddress());
                break;

            case UPDATE:
                // for edit (keep the address ID)
                Address add = station.getAddress();
                Address dtoAdd = dto.getAddress();
                add.setStreetAndHousenumber(dtoAdd.getStreetAndHousenumber());
                add.setZip(dtoAdd.getZip());
                add.setCity(dtoAdd.getCity());
                add.setCountry(dtoAdd.getCountry());
                break;
        }
    }

    /**
    * This method returns the query to get information of all the stations or only the specified station (by stationId)
    *
    */
    private CriteriaQuery<ViewStationDTO> getStationQuery(CriteriaBuilder builder, Long stationId) {
        CriteriaQuery<ViewStationDTO> criteria = builder.createQuery(ViewStationDTO.class);

        Root<Station> root = criteria.from(Station.class);
        Join<Station, StationSlot> slot = root.join("stationSlots", JoinType.LEFT);
        Join<Station, Address> add = root.join("address", JoinType.LEFT);
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
        );

        if (stationId != null) {
            criteria.where(builder.equal(root.get("stationId"), stationId));
        }

        criteria.groupBy(root.get("stationId"), add.get("addressId"));

        return criteria;
    }

}
