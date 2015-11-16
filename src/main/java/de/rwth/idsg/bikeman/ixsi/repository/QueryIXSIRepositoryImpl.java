package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.ChangedProvidersResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.InavailabilityDTO;
import de.rwth.idsg.bikeman.ixsi.dto.PedelecDTO;
import de.rwth.idsg.bikeman.ixsi.dto.PlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.StationDTO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import xjc.schema.ixsi.BookingTargetIDType;
import xjc.schema.ixsi.BookingTargetPropertiesType;
import xjc.schema.ixsi.GeoCircleType;
import xjc.schema.ixsi.GeoRectangleType;
import xjc.schema.ixsi.TimePeriodType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by max on 06/10/14.
 * Repository for handling
 */
@Slf4j
@Repository
public class QueryIXSIRepositoryImpl implements QueryIXSIRepository {

    @PersistenceContext private EntityManager em;

    @Override
    public BookingTargetsInfoResponseDTO bookingTargetInfos() {

        final String pedelecQuery = "SELECT new de.rwth.idsg.bikeman.ixsi.dto." +
                                    "PedelecDTO(p.manufacturerId) " +
                                    "FROM Pedelec p " +
                                    "WHERE p.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE";

        final String stationQuery = "SELECT new de.rwth.idsg.bikeman.ixsi.dto." +
                                    "StationDTO(s.manufacturerId, s.locationLongitude, s.locationLatitude, " +
                                    "(SELECT count(sl) FROM StationSlot sl WHERE s = sl.station AND NOT sl.state = de.rwth.idsg.bikeman.domain.OperationState.DELETED), " +
                                    "s.name, s.note, " +
                                    "a.streetAndHousenumber, a.zip, a.city, a.country) " +
                                    "FROM Station s " +
                                    "LEFT JOIN s.address a " +
                                    "WHERE s.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE";

        List<PedelecDTO> pedelecList = em.createQuery(pedelecQuery, PedelecDTO.class).getResultList();
        List<StationDTO> stationList = em.createQuery(stationQuery, StationDTO.class).getResultList();

        long timestamp = getMaxUpdateTimestamp();

        BookingTargetsInfoResponseDTO dto = new BookingTargetsInfoResponseDTO();
        dto.setPedelecs(pedelecList);
        dto.setStations(stationList);
        dto.setTimestamp(timestamp);
        return dto;
    }

    @Override
    public ChangedProvidersResponseDTO changedProviders(long requestTimestamp) {
        ChangedProvidersResponseDTO responseDTO = new ChangedProvidersResponseDTO();

        long timestamp = getMaxUpdateTimestamp();
        if (requestTimestamp < timestamp) {
            // update necessary!
            responseDTO.setProvidersChanged(true);
        } else {
            responseDTO.setProvidersChanged(false);
        }
        responseDTO.setTimestamp(timestamp);

        return responseDTO;
    }

    private long getMaxUpdateTimestamp() {
        Date pedelecUpdated = em.createQuery("SELECT max(p.updated) FROM Pedelec p", Date.class)
                .getSingleResult();

        Date stationUpdated = em.createQuery("SELECT max(s.updated) FROM Station s", Date.class)
                .getSingleResult();

        return Math.max(pedelecUpdated.getTime(), stationUpdated.getTime());
    }

    private List<BookingTargetPropertiesType> getTargetPropertyList(List<String> ids) {
        List<BookingTargetPropertiesType> res = new ArrayList<>();
        for (String id : ids) {
            BookingTargetPropertiesType t = new BookingTargetPropertiesType();
            BookingTargetIDType bookingTargetIDType = new BookingTargetIDType();
            bookingTargetIDType.setBookeeID(id);
            bookingTargetIDType.setProviderID(IXSIConstants.Provider.id);

            res.add(t);
        }

        return res;
    }

    // -------------------------------------------------------------------------
    // Pedelec availability
    // -------------------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    public List<AvailabilityResponseDTO> availability(List<BookingTargetIDType> targets) {

        // -------------------------------------------------------------------------
        // 1. Get all data as flat tables
        // -------------------------------------------------------------------------

        // Open reservations
        String reservationQuery = "SELECT new de.rwth.idsg.bikeman.ixsi.dto." +
                                  "InavailabilityDTO(p.manufacturerId, r.startDateTime, r.endDateTime) " +
                                  "FROM Reservation r " +
                                  "JOIN r.pedelec p " +
                                  "WHERE p.manufacturerId IN :idList " +
                                  "AND r.state = de.rwth.idsg.bikeman.domain.ReservationState.CREATED " +
                                  "AND (:now BETWEEN r.startDateTime AND r.endDateTime)";

        // Open transactions
        String transactionQuery = "SELECT new de.rwth.idsg.bikeman.ixsi.dto." +
                                  "InavailabilityDTO(p.manufacturerId, t.startDateTime, t.endDateTime) " +
                                  "FROM Transaction t " +
                                  "JOIN t.pedelec p " +
                                  "WHERE p.manufacturerId IN :idList " +
                                  "AND t.endDateTime IS NULL " +
                                  "AND t.toSlot IS NULL";

        String statusQuery = "SELECT new de.rwth.idsg.bikeman.ixsi.dto." +
                             "AvailabilityResponseDTO(p.manufacturerId, s.manufacturerId, cs.batteryStateOfCharge) " +
                             "FROM Pedelec p " +
                             "JOIN p.chargingStatus cs " +
                             "LEFT JOIN p.stationSlot.station s " +
                             "WHERE p.manufacturerId IN :idList " +
                             "AND p.stationSlot.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
                             "AND s.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE ";

        List<String> idList = new ArrayList<>(targets.size());
        for (BookingTargetIDType id : targets) {
            idList.add(id.getBookeeID());
        }

        List<InavailabilityDTO> reservList = em.createQuery(reservationQuery, InavailabilityDTO.class)
                                               .setParameter("idList", idList)
                                               .setParameter("now", new LocalDateTime())
                                               .getResultList();

        List<InavailabilityDTO> transList = em.createQuery(transactionQuery, InavailabilityDTO.class)
                                              .setParameter("idList", idList)
                                              .getResultList();

        List<AvailabilityResponseDTO> responseList = em.createQuery(statusQuery, AvailabilityResponseDTO.class)
                                                       .setParameter("idList", idList)
                                                       .getResultList();

        // -------------------------------------------------------------------------
        // 2. Build the object graph
        // -------------------------------------------------------------------------

        Map<String, List<TimePeriodType>> inavailabilityMap = merge(toMap(reservList), toMap(transList));

        responseList.forEach(p -> p.setInavailabilities(inavailabilityMap.get(p.getManufacturerId())));

        return responseList;
    }

    /**
     * Applies two transformations:
     *
     * 1. Converts the list to map using the manufacturer id as the key.
     *    Result is of the form Map<String, List<InavailabilityDTO>>.
     *
     * 2. Converts the entry value List<InavailabilityDTO> to List<TimePeriodType>
     *    for every entry in the map.
     */
    private Map<String, List<TimePeriodType>> toMap(List<InavailabilityDTO> list) {
        return list.stream()
                   .collect(Collectors.groupingBy(InavailabilityDTO::getPedelecManufacturerId))
                   .entrySet()
                   .parallelStream()
                   .collect(Collectors.toMap(Map.Entry::getKey,
                                             e -> e.getValue()
                                                   .parallelStream()
                                                   .map(i -> new TimePeriodType().withBegin(i.getBegin())
                                                                                 .withEnd(i.getEnd()))
                                                   .collect(Collectors.toList())));
    }

    /**
     * Merges m2 into m1
     */
    private Map<String, List<TimePeriodType>> merge(Map<String, List<TimePeriodType>> m1,
                                                    Map<String, List<TimePeriodType>> m2) {

        m2.forEach((k, v) ->
                m1.merge(k, v, (list1, list2) ->
                        { list1.addAll(list2);
                          return list1; }));
        return m1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AvailabilityResponseDTO> availability(GeoCircleType circle) {
        Query q = em.createNativeQuery(
                "SELECT p.manufacturer_id as manufacturerId, s.station_Id as stationId, " +
                "s.location_Latitude as locationLatitude, s.location_Longitude as locationLongitude, p.state_Of_Charge as stateOfCharge " +
                "FROM t_Pedelec p JOIN t_Station_Slot slot ON p.pedelec_Id = slot.pedelec_Id " +
                "JOIN t_Station s ON s.station_Id = slot.station_Id WHERE st_dwithin(" +
                "st_geographyfromtext('POINT( ' || s.location_Latitude || ' ' || s.location_Longitude || ')')," +
                "CAST(st_makepoint( :lat, :lon ) as geography), :radius)");

        q.setParameter("lat", circle.getCenter().getLatitude());
        q.setParameter("lon", circle.getCenter().getLongitude());
        q.setParameter("radius", circle.getRadius());

        return getAvailabilityResponseDTOs(q);
    }

    @Override
    public List<AvailabilityResponseDTO> availability(GeoRectangleType rectangle) {
        Query q = em.createNativeQuery(
                "SELECT p.manufacturer_id as manufacturerId, s.station_Id as stationId, " +
                "s.location_Latitude as locationLatitude, s.location_Longitude as locationLongitude, p.state_Of_Charge as stateOfCharge " +
                "FROM t_Pedelec p JOIN t_Station_Slot slot ON p.pedelec_Id = slot.pedelec_Id " +
                "JOIN t_Station s ON s.station_Id = slot.station_Id WHERE " +
                "st_contains(st_makeenvelope(:lat1, :lon1, :lat2, :lon2, 4326)," +
                "st_geometryfromtext('POINT( ' || s.location_Latitude || ' ' || s.location_Longitude || ')', 4326))");

        q.setParameter("lat1", rectangle.getUpperLeft().getLatitude());
        q.setParameter("lon1", rectangle.getUpperLeft().getLongitude());
        q.setParameter("lat2", rectangle.getLowerRight().getLatitude());
        q.setParameter("lon2", rectangle.getLowerRight().getLongitude());

        return getAvailabilityResponseDTOs(q);
    }

    @SuppressWarnings("unchecked")
    private List<AvailabilityResponseDTO> getAvailabilityResponseDTOs(Query q) {
        List<AvailabilityResponseDTO> myList = new ArrayList<>();

        List<Object[]> fooList = q.getResultList();
        for (Object[] row : fooList) {
            AvailabilityResponseDTO dto = new AvailabilityResponseDTO(
                    (String) row[0],
                    (String) row[1],
                    (BigDecimal) row[2],
                    (BigDecimal) row[3],
                    (Double) row[4]);

            myList.add(dto);
        }
        return myList;
    }

    // -------------------------------------------------------------------------
    // Station availability
    // -------------------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    public List<PlaceAvailabilityResponseDTO> placeAvailability(List<String> placeIdList) {
        Query q = em.createNativeQuery(
                "SELECT s.manufacturer_id, CAST(count(slot) as Integer) " +
                "FROM t_station s " +
                "LEFT JOIN t_station_slot slot ON s.station_id = slot.station_id " +
                "AND slot.state = 'OPERATIVE' " +
                "AND slot.is_occupied = FALSE " +
                "WHERE s.manufacturer_id IN (:placeIds) " +
                "GROUP BY s.manufacturer_id"
        );

        q.setParameter("placeIds", placeIdList);

        return getPlaceAvailabilityResponseDTOs(q);

//        final String q = "SELECT new de.rwth.idsg.bikeman.ixsi.dto.PlaceAvailabilityResponseDTO(" +
//                         "slot.station.manufacturerId, CAST(count(slot) as Integer) " +
//                         "FROM Station s " +
//                         "LEFT JOIN StationSlot slot " +
//                         "ON slot.station = s " +
//                         "AND slot.isOccupied = false " +
//                         "AND slot.state = de.rwth.idsg.bikeman.domain.OperationState.OPERATIVE " +
//                         "WHERE slot.station.manufacturerId in :placeIds " +
//                         "GROUP by slot.station.manufacturerId";
//
//        return em.createQuery(q, PlaceAvailabilityResponseDTO.class)
//                 .setParameter("placeIds", placeIdList)
//                 .getResultList();
    }

    @Override
    public List<PlaceAvailabilityResponseDTO> placeAvailability(GeoCircleType circle) {
        Query q = em.createNativeQuery(
                "SELECT s.manufacturer_id, CAST(count(slot) as Integer) " +
                "FROM t_Station s " +
                "LEFT JOIN t_Station_slot slot " +
                "ON slot.station_id = s.station_id " +
                "WHERE NOT slot.is_occupied AND " +
                "st_dwithin(st_geographyfromtext('POINT( ' || s.location_Latitude || ' ' || s.location_Longitude || ')'), " +
                "CAST(st_makepoint( :lat, :lon ) as geography), :radius) " +
                "GROUP BY s.manufacturer_id");

        q.setParameter("lat", circle.getCenter().getLatitude());
        q.setParameter("lon", circle.getCenter().getLongitude());
        q.setParameter("radius", circle.getRadius());

        return getPlaceAvailabilityResponseDTOs(q);
    }

    @Override
    public List<PlaceAvailabilityResponseDTO> placeAvailability(GeoRectangleType geoRectangle) {
        Query q = em.createNativeQuery(
                "SELECT s.manufacturer_id, CAST(count(slot) as Integer) " +
                "FROM t_station s " +
                "LEFT JOIN t_station_slot slot " +
                "ON slot.station_id = s.station_id " +
                "WHERE NOT slot.is_occupied AND " +
                "st_contains(st_makeenvelope(:lat1, :lon1, :lat2, :lon2, 4326), " +
                "st_geometryfromtext('POINT( ' || s.location_Latitude || ' ' || s.location_Longitude || ')', 4326)) " +
                "GROUP BY s.manufacturer_id");

        q.setParameter("lat1", geoRectangle.getUpperLeft().getLatitude());
        q.setParameter("lon1", geoRectangle.getUpperLeft().getLongitude());
        q.setParameter("lat2", geoRectangle.getLowerRight().getLatitude());
        q.setParameter("lon2", geoRectangle.getLowerRight().getLongitude());

        return getPlaceAvailabilityResponseDTOs(q);
    }

    @SuppressWarnings("unchecked")
    private List<PlaceAvailabilityResponseDTO> getPlaceAvailabilityResponseDTOs(Query q) {
        List<PlaceAvailabilityResponseDTO> myList = new ArrayList<>();

        List<Object[]> fooList = q.getResultList();
        for (Object[] row : fooList) {
            PlaceAvailabilityResponseDTO dto = new PlaceAvailabilityResponseDTO(
                    (String) row[0],
                    (Integer) row[1]
                    );

            myList.add(dto);
        }
        return myList;
    }
}
