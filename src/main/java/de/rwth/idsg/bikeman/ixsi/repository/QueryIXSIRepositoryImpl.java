package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.ixsi.dto.query.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.ChangedProvidersResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.CloseSessionResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.OpenSessionResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.PedelecDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.PlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.StationDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.TokenGenerationResponseDTO;
import de.rwth.idsg.bikeman.ixsi.schema.GeoCircleType;
import de.rwth.idsg.bikeman.ixsi.schema.GeoRectangleType;
import de.rwth.idsg.bikeman.ixsi.schema.ProviderPlaceIDType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
                                    "PedelecDTO(p.pedelecId, p.manufacturerId, 0) " +
                                    "FROM Pedelec p";

        final String stationQuery = "SELECT new de.rwth.idsg.bikeman.ixsi.dto.query." +
                                    "StationDTO(s.stationId, s.locationLongitude, s.locationLatitude, " +
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

    @Override
    @SuppressWarnings("unchecked")
    public List<AvailabilityResponseDTO> availability(GeoCircleType circle) {
        Integer r = circle.getRadius();
        BigDecimal lat = circle.getCenter().getLatitude();
        BigDecimal lon = circle.getCenter().getLongitude();
        final String availQuery = "SELECT " +
                                  "p.pedelec_Id as pedelecId, s.station_Id as stationId, " +
                                  "s.location_Latitude as locationLatitude, s.location_Longitude as locationLongitude, p.state_Of_Charge as stateOfCharge " +
                                  "FROM t_Pedelec p JOIN t_Station_Slot slot ON p.pedelec_Id = slot.pedelec_Id " +
                                  "JOIN t_Station s ON s.station_Id = slot.station_Id " +
                                  "WHERE st_intersects(" +
                                  "st_geometryfromtext('POINT( ' || s.location_Latitude || ' '" +
                                  " || s.location_Longitude || ')')," +
                                  "st_buffer(CAST(st_makepoint(" + lat + ", " + lon + ") as geography), " + r + "))";


        Query q = em.createNativeQuery(availQuery);
        List<AvailabilityResponseDTO> myList = new ArrayList<>();

        List<Object[]> fooList = q.getResultList();
        for (Object[] row : fooList) {
            AvailabilityResponseDTO dto = new AvailabilityResponseDTO(
                    (BigInteger) row[0],
                    (BigInteger) row[1],
                    (BigDecimal) row[2],
                    (BigDecimal) row[3],
                    (Float) row[4]);

            myList.add(dto);
        }
        return myList;
    }

    @Override
    public List<AvailabilityResponseDTO> availability(GeoRectangleType rectangle) {
        return null;
    }

    @Override
    public List<PlaceAvailabilityResponseDTO> placeAvailability(List<ProviderPlaceIDType> placeIds) {
        return null;
    }

    @Override
    public List<PlaceAvailabilityResponseDTO> placeAvailability(GeoCircleType circle) {
        return null;
    }

    @Override
    public List<PlaceAvailabilityResponseDTO> placeAvailability(GeoRectangleType geoRectangle) {
        return null;
    }

    @Override
    public CloseSessionResponseDTO closeSession() {
        return null;
    }

    @Override
    public OpenSessionResponseDTO openSession() {
        return null;
    }

    @Override
    public TokenGenerationResponseDTO tokenGeneration() {
        return null;
    }
}
