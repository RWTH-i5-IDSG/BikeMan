package de.rwth.idsg.velocity.service;

import de.rwth.idsg.velocity.domain.OperationState;
import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.psinterface.exception.PSInterfaceException;
import de.rwth.idsg.velocity.repository.StationRepository;
import de.rwth.idsg.velocity.web.rest.dto.modify.ChangeStationOperationStateDTO;
import de.rwth.idsg.velocity.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.xml.ws.http.HTTPException;

/**
 * Created by max on 18/08/14.
 */
@Service
@Transactional
@Slf4j
public class StationStateService {

    @Inject
    StationRepository stationRepository;

    private static String baseURL = "http://localhost:8081/";

    public void changeOperationState(CreateEditStationDTO dto) throws DatabaseException, RestClientException {
        long stationId = dto.getStationId();
        OperationState state = dto.getState();

        final ViewStationDTO station = stationRepository.findOne(stationId);

        // states do not match -> notify station of update
        if (!(state.equals(station.getState()))) {
            ChangeStationOperationStateDTO changeDTO = new ChangeStationOperationStateDTO();
            changeDTO.setState(state);
            changeDTO.setSlotPosition(null);

            RestTemplate rt = new RestTemplate();
            rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            rt.getMessageConverters().add(new StringHttpMessageConverter());

            String uri = baseURL + station.getManufacturerId() + "/cmsi/state";

            log.debug(uri);
            log.debug(rt.toString());

            rt.postForObject(uri, changeDTO, String.class);
            stationRepository.update(dto);

        }
    }
}
