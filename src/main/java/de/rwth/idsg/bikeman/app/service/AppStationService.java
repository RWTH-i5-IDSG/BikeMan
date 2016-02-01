package de.rwth.idsg.bikeman.app.service;


import de.rwth.idsg.bikeman.app.dto.ViewPedelecSlotDTO;
import de.rwth.idsg.bikeman.app.dto.ViewStationDTO;
import de.rwth.idsg.bikeman.app.dto.ViewStationSlotsDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.app.repository.AppStationRepository;
import de.rwth.idsg.bikeman.app.repository.AppTransactionRepository;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.psinterface.dto.request.RemoteAuthorizeDTO;
import de.rwth.idsg.bikeman.psinterface.rest.client.StationClient;
import de.rwth.idsg.bikeman.repository.StationRepository;
import de.rwth.idsg.bikeman.repository.StationSlotRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AppStationService {

    @Autowired
    private AppStationRepository appStationRepositoryApp;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationSlotRepository stationSlotRepository;

    @Autowired
    private AppTransactionRepository appTransactionRepository;

    @Autowired
    private AppBookingService appBookingService;

    @Autowired
    private AppPedelecService appPedelecService;

    @Autowired
    private StationClient stationClient;

    public List<ViewStationDTO> getAll() throws DatabaseException {
        return appStationRepositoryApp.findAll();
    }

    public ViewStationDTO get(Long id) throws DatabaseException {
        return appStationRepositoryApp.findOne(id);
    }

    public ViewStationSlotsDTO getSlots(Long id) throws DatabaseException {
        return ViewStationSlotsDTO.builder()
                                  .stationSlots(appStationRepositoryApp.findOneWithSlots(id))
                                  .build();
    }

    public ViewStationSlotsDTO getSlotsWithPreferredSlotId(Long id, Customer customer) throws DatabaseException {
        ViewStationSlotsDTO stationSlotsDTO = this.getSlots(id);

        Optional<Long> optionalBookingSlot = appBookingService.getBookingSlotId(customer);

        if (optionalBookingSlot.isPresent()) {
            stationSlotsDTO.setRecommendedSlot(optionalBookingSlot.get());
        } else {
            Optional<Long> optionalPedelecSlot = appPedelecService.getRecommendedPedelecSlotId(id);

            if (optionalPedelecSlot.isPresent()) {
                stationSlotsDTO.setRecommendedSlot(optionalPedelecSlot.get());
            }
        }

        return stationSlotsDTO;
    }

    @Transactional(readOnly = true)
    public ViewPedelecSlotDTO authorizeRemote(Long stationId, Long stationSlotId, Customer customer, String cardPin)
            throws AppException {

        if (appTransactionRepository.numberOfOpenTransactionsByCustomer(customer) > 0) {
            throw new AppException("Rental is blocked due to a persisting rental.", AppErrorCode.RENTAL_BLOCKED);
        }

        if (!cardPin.equals(customer.getCardAccount().getCardPin())) {
            throw new AppException("Wrong PIN code!", AppErrorCode.AUTH_FAILED);
        }

        StationSlot slot = stationSlotRepository.getOne(stationSlotId);

        if (!slot.getIsOccupied()
                || slot.getState() != OperationState.OPERATIVE
                || slot.getPedelec().getState() != OperationState.OPERATIVE) {
            throw new AppException("Selected Pedelec not available!", AppErrorCode.CONSTRAINT_FAILED);
        }

        RemoteAuthorizeDTO authorizeDTO = RemoteAuthorizeDTO.builder()
                                                            .slotPosition(slot.getStationSlotPosition())
                                                            .cardId(customer.getCardAccount().getCardId())
                                                            .build();

        stationClient.authorizeRemote(stationRepository.getEndpointAddress(stationId), authorizeDTO);

        return ViewPedelecSlotDTO.builder()
                                 .stationSlotId(stationSlotId)
                                 .stationSlotPosition(slot.getStationSlotPosition())
                                 .build();
    }

}
