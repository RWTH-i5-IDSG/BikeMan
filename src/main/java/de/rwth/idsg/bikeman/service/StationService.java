package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.repository.StationRepository;
import de.rwth.idsg.bikeman.repository.StationSlotRepository;
import de.rwth.idsg.bikeman.web.rest.dto.modify.ChangeStationOperationStateDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.StationConfigurationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Created by max on 18/08/14.
 */
@Service
@Transactional
@Slf4j
public class StationService {

    @Inject
    StationRepository stationRepository;

    @Inject
    StationSlotRepository stationSlotRepository;

    private static String baseURL = "http://localhost:8081/";

    public boolean changeStationOperationState(Long id, ChangeStationOperationStateDTO changeDTO) throws DatabaseException, RestClientException {
        long stationId = id;

        final ViewStationDTO station = stationRepository.findOne(stationId);

        // states do not match -> notify station of update
        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());

        String uri = baseURL + station.getManufacturerId() + "/cmsi/state";

        rt.postForObject(uri, changeDTO, String.class);

        return true;
    }

    public StationConfigurationDTO getStationConfig(Long id) throws DatabaseException, RestClientException {
        ViewStationDTO station = stationRepository.findOne(id);
        String manufacturerId = station.getManufacturerId();

        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());

        String uri = baseURL + manufacturerId + "/cmsi/config";

        return rt.getForObject(uri, StationConfigurationDTO.class);
    }

    public void changeStationConfiguration(Long id, StationConfigurationDTO dto) throws RestClientException, DatabaseException {
        ViewStationDTO station = stationRepository.findOne(id);
        String manufacturerId = station.getManufacturerId();

        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());

        String uri = baseURL + manufacturerId + "/cmsi/config";

        rt.postForObject(uri, dto, String.class);
    }

    public void rebootStation(Long id) throws RestClientException, DatabaseException {
        ViewStationDTO station = stationRepository.findOne(id);
        String manufacturerId = station.getManufacturerId();

        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());

        String uri = baseURL + manufacturerId + "/cmsi/reboot";

        rt.postForObject(uri, null, String.class);
    }

    public void updateSlot(Long id, ChangeStationOperationStateDTO dto) throws DatabaseException {
        StationSlot slot = stationSlotRepository.findByStationSlotPositionAndStationStationId(dto.getSlotPosition(), id);

        slot.setState(dto.getState());

        stationSlotRepository.save(slot);
    }
}
