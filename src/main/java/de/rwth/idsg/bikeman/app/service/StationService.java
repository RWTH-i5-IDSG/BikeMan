package de.rwth.idsg.bikeman.app.service;


import de.rwth.idsg.bikeman.app.dto.RemoteAuthorizeDTO;
import de.rwth.idsg.bikeman.app.dto.ViewPedelecSlotDTO;
import de.rwth.idsg.bikeman.app.dto.ViewStationDTO;
import de.rwth.idsg.bikeman.app.dto.ViewStationSlotsDTO;
import de.rwth.idsg.bikeman.app.repository.StationRepository;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.psinterface.rest.client.StationClient;
import de.rwth.idsg.bikeman.repository.StationSlotRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("StationServiceApp")
@Slf4j
public class StationService {

    @Autowired
    @Qualifier("StationRepositoryImplApp")
    private de.rwth.idsg.bikeman.app.repository.StationRepository stationRepositoryApp;

    @Autowired
    @Qualifier("stationRepositoryImpl")
    private de.rwth.idsg.bikeman.repository.StationRepository stationRepository;

    @Autowired
    private StationSlotRepository stationSlotRepository;

    @Autowired
    private StationClient stationClient;

    public List<ViewStationDTO> getAll() throws DatabaseException {
        return stationRepositoryApp.findAll();
    }

    public ViewStationDTO get(Long id) throws DatabaseException {
        return stationRepositoryApp.findOne(id);
    }

    public List<ViewStationSlotsDTO> getSlots(Long id) throws DatabaseException {
        return stationRepositoryApp.findOneWithSlots(id);
    }

    @Transactional(readOnly = true)
    public ViewPedelecSlotDTO authorizeRemote(Long stationId, Long stationSlotId, Customer customer) {
        StationSlot slot = stationSlotRepository.getOne(stationSlotId);

        stationClient.authorizeRemote(
            stationRepository.getEndpointAddress(stationId),

            RemoteAuthorizeDTO.builder()
                .slotPosition(slot.getStationSlotPosition())
                .cardId(customer.getCardAccount().getCardId())
                .build()
        );

        return ViewPedelecSlotDTO.builder()
            .stationSlotId(stationSlotId)
            .stationSlotPosition(slot.getStationSlotPosition())
            .build();
    }

}
