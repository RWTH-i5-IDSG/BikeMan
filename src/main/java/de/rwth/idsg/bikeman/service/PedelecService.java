package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.psinterface.rest.client.PedelecClient;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.web.rest.dto.modify.ChangePedelecOperationStateDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.PedelecConfigurationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewErrorDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewPedelecDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by max on 18/08/14.
 */
@Service
@Slf4j
public class PedelecService {

    @Autowired private PedelecClient pedelecClient;
    @Autowired private PedelecRepository pedelecRepository;
    @Autowired private OperationStateService operationStateService;

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

    public List<ViewErrorDTO> getErrors() throws DatabaseException {
        return pedelecRepository.findErrors()
                                .stream()
                                .sorted((e1, e2) -> e2.getLastUpdated().compareTo(e1.getLastUpdated()))
                                .collect(Collectors.toList());
    }

    public void changeOperationState(CreateEditPedelecDTO dto) throws DatabaseException, RestClientException {
        Pedelec pedelec = pedelecRepository.findOne(dto.getPedelecId());
        checkPedelec(pedelec, "Cannot change operation state.");

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

        // throws PsException
        pedelecClient.changeOperationState(station.getEndpointAddress(), pedelec.getManufacturerId(), changeDto);

        pedelecRepository.update(dto);

        if (dto.getState() == OperationState.INOPERATIVE) {
            operationStateService.pushPedelecInavailability(pedelec.getManufacturerId());
        } else {
            operationStateService.pushPedelecAvailability(pedelec.getManufacturerId());
        }
    }

    public PedelecConfigurationDTO getConfig(Long pedelecId) throws DatabaseException, RestClientException {
        Pedelec pedelec = pedelecRepository.findOne(pedelecId);
        checkPedelec(pedelec, "Cannot get config.");

        Station station = pedelec.getStationSlot().getStation();
        return pedelecClient.getConfig(station.getEndpointAddress(), pedelec.getManufacturerId());
    }

    public void changeConfig(Long pedelecId, PedelecConfigurationDTO dto) throws RestClientException, DatabaseException {
        Pedelec pedelec = pedelecRepository.findOne(pedelecId);
        checkPedelec(pedelec, "Cannot change config.");

        Station station = pedelec.getStationSlot().getStation();
        pedelecClient.changeConfig(station.getEndpointAddress(), pedelec.getManufacturerId(), dto);
    }

    /**
     * Do not permit changes to pedelecs which are currently rented
     */
    private void checkPedelec(Pedelec pedelec, String specialMessage) {
        if (pedelec.getInTransaction()) {
            throw new DatabaseException("The pedelec is in a transaction. " + specialMessage);

        } else if (pedelec.getStationSlot() == null) {
            throw new DatabaseException("The pedelec is not at a station slot. " + specialMessage);
        }
    }
}
