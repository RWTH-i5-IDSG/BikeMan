package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.psinterface.PedelecClient;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.repository.StationRepository;
import de.rwth.idsg.bikeman.web.rest.dto.modify.ChangePedelecOperationStateDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.PedelecConfigurationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;

/**
 * Created by max on 18/08/14.
 */
@Service
@Slf4j
public class PedelecService {

    @Autowired private PedelecClient pedelecClient;
    @Autowired private PedelecRepository pedelecRepository;

    public List<ViewPedelecDTO> getAll() throws DatabaseException {
        return pedelecRepository.findAll();
    }

    public ViewPedelecDTO get(Long pedelecId) throws DatabaseException {
        return pedelecRepository.findOneDTO(pedelecId);
    }

    public void create(CreateEditPedelecDTO dto) throws DatabaseException {
        pedelecRepository.create(dto);
    }

    public void delete(Long pedelecId) throws DatabaseException {
        pedelecRepository.delete(pedelecId);
    }

    public void changeOperationState(CreateEditPedelecDTO dto) throws DatabaseException, RestClientException {
        Pedelec pedelec = pedelecRepository.findOne(dto.getPedelecId());

        OperationState inputState = dto.getState();

        // states do match -> early exit
        if (inputState.equals(pedelec.getState())) {
            return;
        }

        StationSlot slot = pedelec.getStationSlot();
        int stationSlotPosition = slot.getStationSlotPosition();
        Station station = slot.getStation();

        ChangePedelecOperationStateDTO changeDto = new ChangePedelecOperationStateDTO();
        changeDto.setPedelecState(inputState);
        changeDto.setSlotPosition(stationSlotPosition);

        boolean success = pedelecClient.changeOperationState(station.getEndpointAddress(), pedelec.getManufacturerId(), changeDto);
        if (success) {
            pedelecRepository.update(dto);
        }
    }

    public PedelecConfigurationDTO getConfig(Long pedelecId) throws DatabaseException, RestClientException {
        Pedelec pedelec = pedelecRepository.findOne(pedelecId);
        Station station = pedelec.getStationSlot().getStation();
        return pedelecClient.getConfig(station.getEndpointAddress(), pedelec.getManufacturerId());
    }

    public void changeConfig(Long pedelecId, PedelecConfigurationDTO dto) throws RestClientException, DatabaseException {
        Pedelec pedelec = pedelecRepository.findOne(pedelecId);
        Station station = pedelec.getStationSlot().getStation();
        pedelecClient.changeConfig(station.getEndpointAddress(), pedelec.getManufacturerId(), dto);
    }
}
