package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.Reservation;
import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.service.BookingService;
import de.rwth.idsg.bikeman.psinterface.dto.request.CancelReservationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.ReserveNowDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.psinterface.repository.PsiTransactionRepository;
import de.rwth.idsg.bikeman.psinterface.rest.PsiService;
import de.rwth.idsg.bikeman.psinterface.rest.client.StationClient;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.repository.ReservationRepository;
import de.rwth.idsg.bikeman.repository.StationRepository;
import de.rwth.idsg.bikeman.repository.StationSlotRepository;
import de.rwth.idsg.bikeman.security.SecurityUtils;
import de.rwth.idsg.bikeman.web.rest.dto.modify.ChangeStationOperationStateDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CreateEditStationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.modify.StationConfigurationDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewErrorDTO;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewStationDTO;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import de.rwth.idsg.bikeman.web.rest.exception.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired private TransactionEventService transactionEventService;
    @Autowired private PsiService psiService;
    @Autowired private PsiTransactionRepository transactionRepository;
    @Autowired private BookingService bookingService;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private CardAccountService cardAccountService;

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

    public List<ViewErrorDTO> getErrors() throws DatabaseException {
        List<ViewErrorDTO> stationErrors = stationRepository.findErrors();
        List<ViewErrorDTO> stationSlotErrors = stationSlotRepository.findErrors();

        List<ViewErrorDTO> errors = new ArrayList<>(stationErrors);

        errors.addAll(stationSlotErrors);

        return errors
                .stream()
                .sorted((e1, e2) -> e2.getLastUpdated().compareTo(e1.getLastUpdated()))
                .collect(Collectors.toList());
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
        Station station = stationRepository.findByManufacturerId(dto.getManufacturerId());

        // first, communicate with the station to update status (if it is changed)
        // then, update in DB
        if (station.getState() != dto.getState()) {
            ChangeStationOperationStateDTO changeDTO = new ChangeStationOperationStateDTO();
            changeDTO.setState(dto.getState());
            stationClient.changeOperationState(station.getEndpointAddress(), changeDTO);
        }

        stationRepository.update(dto);
        operationStateService.pushStationChange(dto);
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

        operationStateService.pushSlotChange(stationSlot);
    }

    @Async
    public void reserveNow(String endpointAddress, ReserveNowDTO reserveNowDTO) throws DatabaseException {

        stationClient.reserveNow(reserveNowDTO, endpointAddress);
    }

    @Async
    public void cancelReservation(String endpointAddress, CancelReservationDTO cancelReservationDTO)
            throws DatabaseException {

        stationClient.cancelReservation(cancelReservationDTO, endpointAddress);
    }

    @Transactional
    public void unlockSlot(Long stationId, Long stationSlotId) {

        String endpointAddress = stationRepository.getEndpointAddress(stationId);
        StationSlot slot = stationSlotRepository.findOne(stationSlotId);

        List<CardAccount> cardAccounts = cardAccountService.getCardAccountsOfCurrentUser();

        String cardId = "";

        Pedelec pedelec = slot.getPedelec();

        if (cardAccounts.isEmpty()) {
            if (pedelec != null) {
                log.error("Maintainer (id:{}) has no proper CardAccount for unlocking slot (id:{}) with pedelec (id:{})",
                        SecurityUtils.getCurrentLogin(), stationSlotId, pedelec.getPedelecId());
                throw new DatabaseException("No valid card account found.");
            }
        } else {
            cardId = cardAccounts.get(0).getCardId();
        }

        // send unlock command to station
        try {
            stationClient.unlockSlot(slot.getStationSlotPosition(), endpointAddress);
        } catch (PsException e) {
            log.error("Error occured while opening StationSlot with ID: {}", stationSlotId);
        }

        // Without pedelec it is not necessary to start a maintenance transaction
        if (pedelec == null) {
            return;
        }

        // check for reservation for this pedelec and cancel reservations
        List<Reservation> reservations = reservationRepository
                .findByTimeFrameForPedelec(pedelec.getPedelecId(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        reservations.forEach(reservation ->
                bookingService.cancel(reservation.getBooking().getIxsiBookingId(), reservation.getCardAccount().getCardId())
        );

        // when pedelec is at specific slot, put pedelec in "maintenance" transaction
        StartTransactionDTO startTransactionDTO = StartTransactionDTO.builder()
                                                                     .timestamp(DateTime.now())
                                                                     .pedelecManufacturerId(pedelec.getManufacturerId())
                                                                     .slotManufacturerId(slot.getManufacturerId())
                                                                     .stationManufacturerId(slot.getStation().getManufacturerId())
                                                                     .cardId(cardId)
                                                                     .build();

        transactionEventService.createAndSaveStartTransactionEvent(startTransactionDTO);
        Transaction transaction = transactionRepository.start(startTransactionDTO);

        Booking booking = new Booking();
        booking.setTransaction(transaction);
        bookingRepository.save(booking);

        psiService.performStartPush(startTransactionDTO);
    }
}
