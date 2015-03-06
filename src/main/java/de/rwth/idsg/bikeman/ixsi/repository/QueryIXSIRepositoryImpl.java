package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.ChangedProvidersResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.PedelecDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.PlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.StationDTO;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetIDType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetPropertiesType;
import de.rwth.idsg.bikeman.ixsi.schema.GeoCircleType;
import de.rwth.idsg.bikeman.ixsi.schema.GeoRectangleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        // TODO: The value 0 for maxDistance is a placeholder! Pedelec entity has to be expanded to contain such a property
        //
        final String pedelecQuery = "SELECT new de.rwth.idsg.bikeman.ixsi.dto.query." +
                                    "PedelecDTO(p.manufacturerId, 0) " +
                                    "FROM Pedelec p";

        final String stationQuery = "SELECT new de.rwth.idsg.bikeman.ixsi.dto.query." +
                                    "StationDTO(s.manufacturerId, s.locationLongitude, s.locationLatitude, " +
                                    "s.stationSlots.size, s.name, s.note, " +
                                    "a.streetAndHousenumber, a.zip, a.city, a.country) " +
                                    "FROM Station s LEFT JOIN s.address a";

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
        Query q = em.createQuery(
                "SELECT new de.rwth.idsg.bikeman.ixsi.dto.query.AvailabilityResponseDTO(" +
                "p.manufacturerId, s.manufacturerId, p.stateOfCharge) " +
                "FROM Pedelec p JOIN p.stationSlot slot JOIN slot.station s " +
                "WHERE p.manufacturerId in :targets");

        List<String> idList = new ArrayList<>();
        for (BookingTargetIDType id : targets) {
            idList.add(id.getBookeeID());
        }
        q.setParameter("targets", idList);

        return q.getResultList();
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
                    (Float) row[4]);

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
        final String q = "SELECT new de.rwth.idsg.bikeman.ixsi.dto.query.PlaceAvailabilityResponseDTO(" +
                         "slot.station.manufacturerId, CAST(count(slot) as integer)) " +
                         "FROM StationSlot slot " +
                         "WHERE NOT slot.isOccupied = true AND " +
                         "slot.station.manufacturerId in :placeIds " +
                         "GROUP by slot.station.manufacturerId";

        return em.createQuery(q, PlaceAvailabilityResponseDTO.class)
                 .setParameter("placeIds", placeIdList)
                 .getResultList();
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
