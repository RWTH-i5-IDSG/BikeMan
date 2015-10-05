package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.psinterface.rest.client.StationClient;
import de.rwth.idsg.bikeman.repository.StationRepository;
import de.rwth.idsg.bikeman.repository.StationSlotRepository;
import de.rwth.idsg.bikeman.web.rest.dto.modify.ChangeStationOperationStateDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.StationConfigurationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by max on 18/08/14.
 */
@Service
@Slf4j
public class StationService {

    @Autowired private StationClient stationClient;
    @Autowired private StationRepository stationRepository;
    @Autowired private OperationStateService operationStateService;
    @Autowired private StationSlotRepository stationSlotRepository;

    public void create(CreateEditStationDTO dto) throws DatabaseException {
        stationRepository.create(dto);
    }

    public List<ViewStationDTO> getAll() throws DatabaseException {
        return stationRepository.findAll();
    }

    public ViewStationDTO get(Long id) throws DatabaseException {
        return stationRepository.findOne(id);
    }

    public void reboot(Long id) throws DatabaseException {
        String endpointAddress = stationRepository.getEndpointAddress(id);
        stationClient.reboot(endpointAddress);
    }

    public StationConfigurationDTO getConfig(Long id) throws DatabaseException {
        String endpointAddress = stationRepository.getEndpointAddress(id);
        return stationClient.getConfig(endpointAddress);
    }

    public void updateConfig(Long id, StationConfigurationDTO dto) throws DatabaseException {
// TODO: cmsURI != endpointAddress. cmsURI refers from station to cms.

        String endpointAddress = stationRepository.getEndpointAddress(id);
//        String inputEndpointAddress = dto.getCmsURI();
//
//        if (!endpointAddress.equals(inputEndpointAddress)) {
//            stationRepository.updateEndpointAddress(id, inputEndpointAddress);
//            endpointAddress = inputEndpointAddress;
//        }

        stationClient.changeConfig(endpointAddress, dto);
    }

    public void updateStation(CreateEditStationDTO dto) throws DatabaseException {
        ChangeStationOperationStateDTO changeDTO = new ChangeStationOperationStateDTO();
        changeDTO.setState(dto.getState());

        // first, communicate with the station to update status
        // then, update in DB
        String endpointAddress = stationRepository.getEndpointAddress(dto.getStationId());
        stationClient.changeOperationState(endpointAddress, changeDTO);
        stationRepository.update(dto);

        if (dto.getState() == OperationState.INOPERATIVE) {
            operationStateService.pushStationInavailability(dto.getManufacturerId());
        } else if (dto.getState() == OperationState.OPERATIVE) {
            operationStateService.pushStationAvailability(dto.getManufacturerId());
        }
    }

    public void changeSlotState(Long stationId, ChangeStationOperationStateDTO dto) throws DatabaseException {
        ChangeStationOperationStateDTO changeDTO = new ChangeStationOperationStateDTO();
        changeDTO.setState(dto.getState());
        changeDTO.setSlotPosition(dto.getSlotPosition());

        // first, communicate with the station to update status
        // then, update in DB
        String endpointAddress = stationRepository.getEndpointAddress(stationId);
        stationClient.changeOperationState(endpointAddress, changeDTO);
        stationRepository.changeSlotState(stationId, dto.getSlotPosition(), dto.getState());

        StationSlot stationSlot = stationSlotRepository.findByStationSlotPositionAndStationStationId(dto.getSlotPosition(), stationId);

        if (dto.getState() == OperationState.INOPERATIVE) {
            operationStateService.pushSlotInavailability(stationSlot.getManufacturerId());
        } else if (dto.getState() == OperationState.OPERATIVE) {
            operationStateService.pushSlotAvailability(stationSlot.getManufacturerId());
        }
    }
}
