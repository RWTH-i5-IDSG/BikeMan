package de.rwth.idsg.velocity.service;

import de.rwth.idsg.velocity.domain.OperationState;
import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.domain.Station;
import de.rwth.idsg.velocity.psinterface.exception.PSInterfaceException;
import de.rwth.idsg.velocity.repository.PedelecRepository;
import de.rwth.idsg.velocity.repository.StationRepository;
import de.rwth.idsg.velocity.web.rest.dto.modify.*;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewPedelecDTO;
import de.rwth.idsg.velocity.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
public class PedelecService {

    @Inject
    StationRepository stationRepository;

    @Inject
    PedelecRepository pedelecRepository;

    private static String baseURL = "http://localhost:8081/";

    public void changeOperationState(CreateEditPedelecDTO dto) throws DatabaseException, RestClientException {
        long pedelecId = dto.getPedelecId();

        OperationState state = dto.getState();

        final Pedelec pedelec = pedelecRepository.findOne(pedelecId);
        final Station station = pedelec.getStationSlot().getStation();
        final int stationSlotPosition = pedelec.getStationSlot().getStationSlotPosition();

        // states do not match -> notify station of update
        if (!(state.equals(pedelec.getState()))) {
            ChangePedelecOperationStateDTO changeDTO = new ChangePedelecOperationStateDTO();
            changeDTO.setPedelecState(state);
            changeDTO.setSlotPosition(stationSlotPosition);

            RestTemplate rt = new RestTemplate();

            String uri = baseURL + station.getManufacturerId() + "/cmsi/pedelecs/" + pedelec.getManufacturerId() + "/state";

            rt.postForObject(uri, changeDTO, String.class);
            pedelecRepository.update(dto);
        }
    }

    public PedelecConfigurationDTO getPedelecConfig(long id) throws DatabaseException, RestClientException {
        Pedelec pedelec = pedelecRepository.findOne(id);
        String manufacturerId = pedelec.getManufacturerId();
        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();

        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());

        String uri = baseURL + stationManufacturerId + "/cmsi/pedelecs/" + manufacturerId + "/config";

        return rt.getForObject(uri, PedelecConfigurationDTO.class);
    }

    public void changePedelecConfiguration(long id, PedelecConfigurationDTO dto) throws RestClientException, DatabaseException {
        Pedelec pedelec = pedelecRepository.findOne(id);
        String manufacturerId = pedelec.getManufacturerId();
        String stationManufacturerId = pedelec.getStationSlot().getStation().getManufacturerId();


        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());

        String uri = baseURL + stationManufacturerId + "/cmsi/pedelecs/" + manufacturerId + "/config";

        rt.postForObject(uri, dto, String.class);
    }
}
