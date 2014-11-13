package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Address;
import de.rwth.idsg.bikeman.domain.Address_;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Pedelec_;
import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.domain.StationSlot_;
import de.rwth.idsg.bikeman.domain.Station_;
import de.rwth.idsg.bikeman.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.SlotDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditAddressDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewStationSlotDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sgokay on 26.05.14.
 */
@Repository
@Slf4j
public class StationRepositoryImpl implements StationRepository {

    private enum Operation { CREATE, UPDATE };

    @PersistenceContext private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<ViewStationDTO> findAll() throws DatabaseException {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<ViewStationDTO> criteria = this.getStationQuery(builder, null);
            return em.createQuery(criteria).getResultList();

        } catch (Exception e) {
            throw new DatabaseException("Failed during database operation.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStationDTO> findByLocation(BigDecimal latitude, BigDecimal longitude)  throws DatabaseException {
        // TODO
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public ViewStationDTO findOne(long stationId) throws DatabaseException {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        // get station info
        CriteriaQuery<ViewStationDTO> criteria = this.getStationQuery(builder, stationId);
        ViewStationDTO stat;
        try {
            stat = em.createQuery(criteria).getSingleResult();
        } catch (Exception e) {
            throw new DatabaseException("Failed to find station with stationId " + stationId, e);
        }

        // get slots for the station
        CriteriaQuery<ViewStationSlotDTO> slotCriteria = builder.createQuery(ViewStationSlotDTO.class);
        Root<StationSlot> stationSlot = slotCriteria.from(StationSlot.class);
        Join<StationSlot, Pedelec> pedelec = stationSlot.join(StationSlot_.pedelec, JoinType.LEFT);

        slotCriteria.select(
                builder.construct(
                        ViewStationSlotDTO.class,
                        stationSlot.get(StationSlot_.stationSlotId),
                        stationSlot.get(StationSlot_.manufacturerId),
                        stationSlot.get(StationSlot_.stationSlotPosition),
                        stationSlot.get(StationSlot_.state),
                        stationSlot.get(StationSlot_.isOccupied),
                        pedelec.get(Pedelec_.pedelecId),
                        pedelec.get(Pedelec_.manufacturerId)
                )
        ).where(builder.equal(stationSlot.get(StationSlot_.station).get(Station_.stationId), stationId));

        try {
            List<ViewStationSlotDTO> list = em.createQuery(slotCriteria).getResultList();
            stat.setSlots(list);
        } catch (Exception e) {
            throw new DatabaseException("Failed to get slots for the station", e);
        }

        return stat;
    }

    @Override
    @Transactional(readOnly = true)
    public String getEndpointAddress(long stationId) throws DatabaseException {
        final String q = "SELECT s.endpointAddress FROM Station s WHERE s.stationId = :stationId";
        try {
            return em.createQuery(q, String.class)
                     .setParameter("stationId", stationId)
                     .getSingleResult();
        } catch (Exception e) {
            throw new DatabaseException("Failed to find station with stationId " + stationId, e);
        }
    }

    @Override
    public void updateEndpointAddress(long stationId, String endpointAddress) throws DatabaseException {
        final String q = "UPDATE Station s SET s.endpointAddress = :endpointAddress WHERE s.stationId = :stationId";
        try {
            em.createQuery(q)
              .setParameter("stationId", stationId)
              .setParameter("endpointAddress", endpointAddress)
              .getSingleResult();
        } catch (Exception e) {
            throw new DatabaseException("Failed to update endpointAddress of station with stationId " + stationId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateEditStationDTO dto) throws DatabaseException {
        Station station = new Station();
        setFields(station, dto, Operation.CREATE);

        try {
            em.persist(station);
            log.debug("Created new manager {}", station);

        } catch (EntityExistsException e) {
            throw new DatabaseException("This station exists already.", e);

        } catch (Exception e) {
            throw new DatabaseException("Failed to create a new station.", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CreateEditStationDTO dto) throws DatabaseException {
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
            throw new DatabaseException("Failed to update station with stationId " + stationId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long stationId) throws DatabaseException {
        Station station = getStationEntity(stationId);
        try {
            em.remove(station);
            log.debug("Deleted station {}", station);
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete station with stationId " + stationId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAfterBoot(BootNotificationDTO dto) throws DatabaseException {

        Query findStationQuery = em.createQuery("select s from Station s where s.manufacturerId = :stationManufacturerId");
        findStationQuery.setParameter("stationManufacturerId", dto.getStationManufacturerId());

        Station station = null;

        try {
            station = (Station) findStationQuery.getSingleResult();
        } catch (NoResultException ex) {
        }

        if (station == null) {
            // create new station with new slots
            station = new Station();
            station.setManufacturerId(dto.getStationManufacturerId());
            station.setFirmwareVersion(dto.getFirmwareVersion());

            Set<StationSlot> stationSlots = new HashSet<>();

            for (SlotDTO slotDTO : dto.getSlotDTOs()) {
                StationSlot newStationSlot = new StationSlot();
                newStationSlot.setManufacturerId(slotDTO.getSlotManufacturerId());
                newStationSlot.setErrorCode(slotDTO.getSlotErrorCode());
                newStationSlot.setErrorInfo(slotDTO.getSlotErrorInfo());
                newStationSlot.setStationSlotPosition(slotDTO.getSlotPosition());
                newStationSlot.setStation(station);
                newStationSlot.setState(OperationState.valueOf(slotDTO.getSlotState().toString()));

                if (slotDTO.getPedelecManufacturerId() != null) {
                    Query findPedelec = em.createQuery("select p from Pedelec p where p.manufacturerId = :manufacturerId");
                    findPedelec.setParameter("manufacturerId", slotDTO.getPedelecManufacturerId());

                    Pedelec pedelec = null;
                    try {
                        pedelec = (Pedelec) findPedelec.getSingleResult();
                    } catch (NoResultException ex) {
                    }

                    if (pedelec == null) {
                        pedelec = new Pedelec();
                    }

                    StationSlot pedelecStationSlot = pedelec.getStationSlot();

                    if (pedelecStationSlot != null) {
                        pedelecStationSlot.setPedelec(null);
                        em.merge(pedelecStationSlot);
                    }

                    pedelec.setManufacturerId(slotDTO.getPedelecManufacturerId());
                    pedelec.setStationSlot(newStationSlot);
                    newStationSlot.setPedelec(pedelec);
                    newStationSlot.setIsOccupied(true);
                    //em.merge(pedelec);
                } else {
                    newStationSlot.setIsOccupied(false);
                }

                stationSlots.add(newStationSlot);
            }

            station.setStationSlots(stationSlots);

            for (StationSlot slot : stationSlots) {
                Pedelec pedelec = slot.getPedelec();
                if (pedelec != null) {
                    em.persist(pedelec);
                }
            }

            em.persist(station);

            //save pedelec, save slot, save station

        } else {
            station.setFirmwareVersion(dto.getFirmwareVersion());

            Set<StationSlot> stationSlots = new HashSet<>();

            for (StationSlot slot : station.getStationSlots()) {
                for (SlotDTO slotDTO : dto.getSlotDTOs()) {
                    if (!slot.getManufacturerId().equals(slotDTO.getSlotManufacturerId())) {
                        continue;
                    }

                    slot.setManufacturerId(slotDTO.getSlotManufacturerId());
                    slot.setErrorCode(slotDTO.getSlotErrorCode());
                    slot.setErrorInfo(slotDTO.getSlotErrorInfo());
                    slot.setStationSlotPosition(slotDTO.getSlotPosition());
                    slot.setStation(station);
                    slot.setState(OperationState.valueOf(slotDTO.getSlotState().toString()));

                    if (slot.getPedelec() == null && slotDTO.getPedelecManufacturerId() == null) {
                        //no pedelec update
                        slot.setIsOccupied(false);
                        continue;
                    } else if (slotDTO.getPedelecManufacturerId().equals(slot.getPedelec().getManufacturerId())) {
                        //still same pedelec
                        slot.setIsOccupied(true);
                        continue;
                    } else {
                        Query findPedelec = em.createQuery("select p from Pedelec p where p.manufacturerId = :manufacturerId");
                        findPedelec.setParameter("manufacturerId", slotDTO.getPedelecManufacturerId());

                        Pedelec pedelec = null;
                        try {
                            pedelec = (Pedelec) findPedelec.getSingleResult();
                        } catch (NoResultException ex) {
                        }

                        if (pedelec == null) {
                            pedelec = new Pedelec();
                        }

                        if (pedelec.getStationSlot() != null) {
                            pedelec.getStationSlot().setPedelec(null);
                            em.merge(pedelec.getStationSlot());
                        }

                        pedelec.setManufacturerId(slotDTO.getPedelecManufacturerId());
                        pedelec.setStationSlot(slot);
                        slot.setPedelec(pedelec);
                        slot.setIsOccupied(true);
                        em.merge(pedelec);
                    }
                }

                stationSlots.add(slot);
            }

            station.setStationSlots(stationSlots);
            em.merge(station);
        }
    }

//        String stationManufacturerId = dto.getStationManufacturerId();
//
//        Station s = new Station();
//        s.setManufacturerId(stationManufacturerId);
//
//        Session session = (Session) em.getDelegate();
//        Criteria criteria = session.createCriteria(Station.class).add(Example.create(s));
//        List results = criteria.list();
//        Station station = (Station) results.get(0);

//        Set<StationSlot> slots = station.getStationSlots();
//        for (StationSlot ss : slots) {
//            log.info("Slot: {}", ss);
//        }
//
//        log.info("Station: {}", station);

//        // -------------------------------------------------------------------------
//        // 1. Update station table
//        // -------------------------------------------------------------------------
//
//        final String sQuery = "UPDATE Station " +
//                              "SET firmwareVersion = :fwVersion " +
//                              "WHERE manufacturerId = :manufacturerId";
//
//        int updateCount = em.createQuery(sQuery)
//                            .setParameter("fwVersion", dto.getFirmwareVersion())
//                            .setParameter("manufacturerId", stationManufacturerId)
//                            .executeUpdate();
//
//        if (updateCount == 1) {
//            log.debug("[StationId: {}] Station info is updated", stationManufacturerId);
//        } else {
//            log.error("[StationId: {}] Station info update FAILED", stationManufacturerId);
//        }
//
//        // -------------------------------------------------------------------------
//        // 2. Insert new and unknown slots
//        // -------------------------------------------------------------------------
//
//        List<SlotDTO> slotDTOs = dto.getSlotDTOs();
//
//        List results = em.createQuery("SELECT ss FROM StationSlot ss " +
//                "WHERE ss.station = (SELECT s FROM Station s WHERE s.manufacturerId = :stationManufacturerId)")
//                .setParameter("stationManufacturerId", stationManufacturerId)
//                .getResultList();
//
//        for (Object result : results) {
//            StationSlot ss = (StationSlot) result;
//            log.info("StationSlot: {}", ss);
//        }


//
//        //TODO
//
//        // -------------------------------------------------------------------------
//        // 3. Batch update station slot table
//        //
//        // TODO: Find out how to batch update with JPA. Current version is BS.
//        // -------------------------------------------------------------------------
//
//        final String ssQuery = "UPDATE StationSlot " +
//                               "SET state = :slotState, " +
//                               "pedelec = (SELECT p FROM Pedelec p WHERE p.manufacturerId = :pedelecManufacturerId), " +
//                               "isOccupied = CASE WHEN :pedelecManufacturerId IS NULL THEN false ELSE true END " +
//                               "WHERE manufacturerId = :slotManufacturerId " +
//                               "AND stationSlotPosition = :slotPosition";
//
//        List<String> failedSlots = new ArrayList<>();
//        for (SlotDTO temp : slotDTOs) {
//            String id = temp.getSlotManufacturerId();
//            int tempCount = em.createQuery(ssQuery)
//                              .setParameter("slotState", temp.getSlotState())
//                              .setParameter("pedelecManufacturerId", temp.getPedelecManufacturerId())
//                              .setParameter("slotManufacturerId", id)
//                              .setParameter("slotPosition", temp.getSlotPosition())
//                              .executeUpdate();
//
//            if (tempCount != 1) {
//                failedSlots.add(id);
//            }
//        }
//
//        int slotsCount = slotDTOs.size();
//        if (failedSlots.size() == 0) {
//            log.debug("[StationId: {}] {} slots to update, ALL are updated.",
//                    stationManufacturerId, slotsCount);
//        } else {
//            log.error("[StationId: {}] {} slots to update, but there are failed updates. List of failed slotIds: {}",
//                    stationManufacturerId, slotsCount, failedSlots);
//        }
//    }

//    @Override
//    public void updateStatus(StationStatusDTO dto) {
//        Station station = (Station) em.createQuery("SELECT s FROM Station s WHERE s.manufacturerId = :manufacturerId")
//                .setParameter("manufacturerId", dto.getStationManufacturerId())
//                .getSingleResult();
//
//
//        Station station = null;
//        try {
//            station = getStationEntity(1);
//            log.info("Got station: {}", station);
//
//            Set<StationSlot> slots = station.getStationSlots();
//            log.info("Got slots: {}", slots);
//
//        } catch (DatabaseException e) {
//            e.printStackTrace();
//        }
//
//
//
//        StationSlot ss = (StationSlot) em.createQuery("SELECT ss FROM StationSlot ss WHERE ss.manufacturerId IN :manufacturerId")
//                .setParameter("manufacturerId", dto.getStationManufacturerId())
//                .getSingleResult();
//
//
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeSlotState(long stationId, int slotPosition, OperationState state) {
        final String query = "UPDATE StationSlot ss " +
                             "SET ss.state = :state " +
                             "WHERE ss.stationSlotPosition = :slotPosition " +
                             "AND ss.station = (SELECT s FROM Station s WHERE s.stationId = :stationId)";

        int count = em.createQuery(query)
                      .setParameter("state", state)
                      .setParameter("slotPosition", slotPosition)
                      .setParameter("stationId", stationId)
                      .executeUpdate();

        if (count == 1) {
            log.debug("[StationId: {}] Slot with position {} is updated", stationId, slotPosition);
        } else {
            log.error("[StationId: {}] Slot with position {} update FAILED", stationId, slotPosition);
        }
    }

    /**
     * Returns a station, or throws exception when no station exists.
     *
     */
    @Transactional(readOnly = true)
    private Station getStationEntity(long stationId) throws DatabaseException {
        Station station = em.find(Station.class, stationId);
        if (station == null) {
            throw new DatabaseException("No station with stationId " + stationId);
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
                Address newAdd = new Address();
                CreateEditAddressDTO newDtoAdd = dto.getAddress();
                newAdd.setStreetAndHousenumber(newDtoAdd.getStreetAndHousenumber());
                newAdd.setZip(newDtoAdd.getZip());
                newAdd.setCity(newDtoAdd.getCity());
                newAdd.setCountry(newDtoAdd.getCountry());
                station.setAddress(newAdd);
                break;

            case UPDATE:
                // for edit (keep the address ID)
                Address add = station.getAddress();
                CreateEditAddressDTO dtoAdd = dto.getAddress();
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

        Root<Station> station = criteria.from(Station.class);
        Join<Station, StationSlot> stationSlot = station.join(Station_.stationSlots, JoinType.LEFT);
        Join<Station, Address> address = station.join(Station_.address, JoinType.LEFT);

        Path<Boolean> occ = stationSlot.get(StationSlot_.isOccupied);

        criteria.select(
                builder.construct(
                        ViewStationDTO.class,
                        station.get(Station_.stationId),
                        station.get(Station_.manufacturerId),
                        station.get(Station_.name),
                        address.get(Address_.streetAndHousenumber),
                        address.get(Address_.zip),
                        address.get(Address_.city),
                        address.get(Address_.country),
                        station.get(Station_.locationLatitude),
                        station.get(Station_.locationLongitude),
                        station.get(Station_.note),
                        station.get(Station_.state),
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

        criteria.groupBy(station.get(Station_.stationId), address.get(Address_.addressId));

        return criteria;
    }

}
