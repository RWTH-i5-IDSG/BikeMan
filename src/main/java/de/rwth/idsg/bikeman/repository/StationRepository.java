package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data JPA repository for the Station entity.
 */
public interface StationRepository {

    List<ViewStationDTO> findAll() throws DatabaseException;
    List<ViewStationDTO> findByLocation(BigDecimal latitude, BigDecimal longitude) throws DatabaseException;
    ViewStationDTO findOne(long stationId) throws DatabaseException;

    Station findOneByManufacturerId(String manufacturerId) throws DatabaseException;

    Long getStationIdByEndpointAddress(String endpointAddress) throws DatabaseException;
    String getEndpointAddress(long stationId) throws DatabaseException;
    void updateEndpointAddress(long stationId, String endpointAddress) throws DatabaseException;

    void create(CreateEditStationDTO dto) throws DatabaseException;
    void update(CreateEditStationDTO dto) throws DatabaseException;
    void delete(long stationId) throws DatabaseException;

    void updateAfterBoot(BootNotificationDTO dto, String endpointAddress) throws DatabaseException;
    void changeSlotState(long stationId, int slotPosition, OperationState state);

//    @Query("SELECT bs FROM Station bs ORDER BY ((6371 * 2 * ASIN(SQRT(POWER(SIN((bs.locationLatitude - abs(:latitude)) * pi()/180 / 2),2) +" +
//            "COS(bs.locationLatitude * pi()/180 ) * COS(abs(:latitude) * pi()/180) *" +
//            "POWER(SIN((bs.locationLongitude - :longitude) * pi()/180 / 2), 2))))*1000) ASC")
//    List<Station> findByLocation(@Param("latitude") BigDecimal latitude, @Param("longitude") BigDecimal longitude);
//
//    @Query("select new de.rwth.idsg.bikeman.web.rest.dto.StationDTO(st.name, st.locationLatitude, st.locationLongitude, st.address, size(st.stationSlots)) from Station st group by st")
//    List<StationDTO> listOfStations();

}