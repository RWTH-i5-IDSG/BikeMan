package de.rwth.idsg.velocity.repository;

import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.web.rest.BackendException;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data JPA repository for the Station entity.
 */
public interface StationRepository {

    List<ViewStationDTO> findAll();
    List<ViewStationDTO> findByLocation(BigDecimal latitude, BigDecimal longitude) throws BackendException;
    ViewStationDTO findOne(long stationId) throws BackendException;
    void create(CreateEditStationDTO dto) throws BackendException;
    void update(CreateEditStationDTO dto) throws BackendException;
    void delete(long stationId) throws BackendException;

//    @Query("SELECT bs FROM Station bs ORDER BY ((6371 * 2 * ASIN(SQRT(POWER(SIN((bs.locationLatitude - abs(:latitude)) * pi()/180 / 2),2) +" +
//            "COS(bs.locationLatitude * pi()/180 ) * COS(abs(:latitude) * pi()/180) *" +
//            "POWER(SIN((bs.locationLongitude - :longitude) * pi()/180 / 2), 2))))*1000) ASC")
//    List<Station> findByLocation(@Param("latitude") BigDecimal latitude, @Param("longitude") BigDecimal longitude);
//
//    @Query("select new de.rwth.idsg.velocity.web.rest.dto.StationDTO(st.name, st.locationLatitude, st.locationLongitude, st.address, size(st.stationSlots)) from Station st group by st")
//    List<StationDTO> listOfStations();

}
