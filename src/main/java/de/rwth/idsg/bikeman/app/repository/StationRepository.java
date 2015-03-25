package de.rwth.idsg.bikeman.app.repository;

import de.rwth.idsg.bikeman.app.dto.ViewStationSlotsDTO;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import de.rwth.idsg.bikeman.app.dto.ViewStationDTO;

import java.util.List;

public interface StationRepository {

    List<ViewStationDTO> findAll() throws AppException;
    ViewStationDTO findOne(long stationId) throws AppException;
    List<ViewStationSlotsDTO> findOneWithSlots(long stationId) throws AppException;


//    @Query("SELECT bs FROM Station bs ORDER BY ((6371 * 2 * ASIN(SQRT(POWER(SIN((bs.locationLatitude - abs(:latitude)) * pi()/180 / 2),2) +" +
//            "COS(bs.locationLatitude * pi()/180 ) * COS(abs(:latitude) * pi()/180) *" +
//            "POWER(SIN((bs.locationLongitude - :longitude) * pi()/180 / 2), 2))))*1000) ASC")
//    List<Station> findByLocation(@Param("latitude") BigDecimal latitude, @Param("longitude") BigDecimal longitude);
//
//    @Query("select new de.rwth.idsg.bikeman.web.rest.dto.StationDTO(st.name, st.locationLatitude, st.locationLongitude, st.address, size(st.stationSlots)) from Station st group by st")
//    List<StationDTO> listOfStations();

}