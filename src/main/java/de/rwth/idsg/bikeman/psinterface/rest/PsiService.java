package de.rwth.idsg.bikeman.psinterface.rest;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Reservation;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.service.AvailabilityPushService;
import de.rwth.idsg.bikeman.ixsi.service.ConsumptionPushService;
import de.rwth.idsg.bikeman.ixsi.service.ExternalBookingPushService;
import de.rwth.idsg.bikeman.ixsi.service.PlaceAvailabilityPushService;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.AccountState;
import de.rwth.idsg.bikeman.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.ChargingStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.CustomerAuthorizeDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.PedelecStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StationStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.AuthorizeConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.BootConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.CardKeyDTO;
import de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.psinterface.repository.PsiBookingRepository;
import de.rwth.idsg.bikeman.psinterface.repository.PsiCustomerRepository;
import de.rwth.idsg.bikeman.psinterface.repository.PsiPedelecRepository;
import de.rwth.idsg.bikeman.psinterface.repository.PsiReservationRepository;
import de.rwth.idsg.bikeman.psinterface.repository.PsiStationRepository;
import de.rwth.idsg.bikeman.psinterface.repository.PsiTransactionRepository;
import de.rwth.idsg.bikeman.service.TransactionEventService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by swam on 04/08/14.
 */

@Service
@Slf4j
public class PsiService {

    @Inject private PsiCustomerRepository customerRepository;
    @Inject private PsiTransactionRepository transactionRepository;
    @Inject private PsiStationRepository stationRepository;
    @Inject private PsiBookingRepository bookingRepository;
    @Inject private PsiPedelecRepository pedelecRepository;
    @Inject private PsiReservationRepository reservationRepository;

    @Inject private ConsumptionPushService consumptionPushService;
    @Inject private AvailabilityPushService availabilityPushService;
    @Inject private PlaceAvailabilityPushService placeAvailabilityPushService;
    @Inject private ExternalBookingPushService externalBookingPushService;
    @Inject private TransactionEventService transactionEventService;

    private static final Integer HEARTBEAT_INTERVAL_IN_SECONDS = 60;

    public BootConfirmationDTO handleBootNotification(BootNotificationDTO bootNotificationDTO,
                                                      String endpointAddress) throws DatabaseException {

        stationRepository.updateAfterBoot(bootNotificationDTO, endpointAddress);
        List<CardKeyDTO> cardKeys = stationRepository.getCardKeys();

        BootConfirmationDTO bootConfirmationDTO = new BootConfirmationDTO();
        bootConfirmationDTO.setTimestamp(Utils.nowInSeconds());
        bootConfirmationDTO.setHeartbeatInterval(HEARTBEAT_INTERVAL_IN_SECONDS);
        bootConfirmationDTO.setCardKeys(cardKeys);
        return bootConfirmationDTO;
    }

    public AuthorizeConfirmationDTO handleAuthorize(CustomerAuthorizeDTO customerAuthorizeDTO) throws DatabaseException {

        log.info("Card with CardId {} start authorization from Station", customerAuthorizeDTO.getCardId());

        CardAccount cardAccount = customerRepository.findByCardId(customerAuthorizeDTO.getCardId());

        // CardAccount not operational
        if (!OperationState.OPERATIVE.equals(cardAccount.getOperationState())) {

            if (cardAccount.getAuthenticationTrialCount() >= 3) {
                log.info("Card with CardId {} authorization failed with {}", customerAuthorizeDTO.getCardId(), PsErrorCode.AUTH_ATTEMPTS_EXCEEDED);
                throw new PsException("Card is not operational!", PsErrorCode.AUTH_ATTEMPTS_EXCEEDED);
            }
            log.info("Card with CardId {} authorization failed with {}", customerAuthorizeDTO.getCardId(), PsErrorCode.CONSTRAINT_FAILED);
            throw new PsException("Card is not operational!", PsErrorCode.CONSTRAINT_FAILED);
        }

        // PIN not correct
        if (!cardAccount.getCardPin().equals(customerAuthorizeDTO.getCardPin())) {

            // increase auth fail count by one
            cardAccount.setAuthenticationTrialCount(cardAccount.getAuthenticationTrialCount()+1);

            // auth attempts exceeded
            if (cardAccount.getAuthenticationTrialCount() >= 3) {
                cardAccount.setOperationState(OperationState.INOPERATIVE);
            }

            customerRepository.saveCardAccount(cardAccount);

            if (cardAccount.getAuthenticationTrialCount() >= 3) {
                log.info("Card with CardId {} authorization failed (3x wrong pin) with {}", customerAuthorizeDTO.getCardId(), PsErrorCode.AUTH_ATTEMPTS_EXCEEDED);
                throw new PsException("Card is disabled, because of wrong PIN!", PsErrorCode.AUTH_ATTEMPTS_EXCEEDED);
            }

            log.info("Card with CardId {} authorization failed (wrong pin) with {}", customerAuthorizeDTO.getCardId(), PsErrorCode.CONSTRAINT_FAILED);
            throw new PsException("Wrong PIN", PsErrorCode.CONSTRAINT_FAILED);
        } else {
            // PIN is correct, reset auth trial count
            customerRepository.resetAuthenticationTrialCount(cardAccount);
        }

        // Is the user allowed to rent?
        AccountState accountState = AccountState.HAS_NO_PEDELEC;
        if (cardAccount.getInTransaction()) {
            accountState = AccountState.HAS_PEDELEC;
        }

        return new AuthorizeConfirmationDTO(cardAccount.getCardId(), accountState);
    }

    public void handleStartTransaction(StartTransactionDTO startTransactionDTO) throws DatabaseException {
        transactionEventService.createAndSaveStartTransactionEvent(startTransactionDTO);

        Transaction t = transactionRepository.start(startTransactionDTO);

        List<Reservation> reservationList = reservationRepository.find(t.getCardAccount().getCardAccountId(),
                                                                       t.getPedelec().getPedelecId(),
                                                                       t.getStartDateTime());

        Booking booking;

        if (reservationList == null || reservationList.isEmpty()) {
            booking = new Booking();

        } else if (reservationList.size() == 1) {
            booking = bookingRepository.findByReservation(reservationList.get(0));

        } else {
            throw new PsException("More than one reservation found", PsErrorCode.CONSTRAINT_FAILED);
        }

        booking.setTransaction(t);
        bookingRepository.save(booking);

        performStartPush(startTransactionDTO, t, booking);
    }

    @Async
    private void performStartPush(StartTransactionDTO startTransactionDTO, Transaction t, Booking booking) {
        externalBookingPushService.report(booking, t);

        Long timestampInMillis = Utils.toMillis(startTransactionDTO.getTimestamp());

        availabilityPushService.takenFromPlace(
            startTransactionDTO.getPedelecManufacturerId(),
            startTransactionDTO.getStationManufacturerId(),
            new DateTime(timestampInMillis));

        placeAvailabilityPushService.reportChange(startTransactionDTO.getStationManufacturerId());
    }

    public void handleStopTransaction(StopTransactionDTO stopTransactionDTO) throws DatabaseException {
        transactionEventService.createAndSaveStopTransactionEvent(stopTransactionDTO);

        Transaction t = transactionRepository.stop(stopTransactionDTO);

        performStopPush(stopTransactionDTO, t);
    }

    @Async
    private void performStopPush(StopTransactionDTO stopTransactionDTO, Transaction t) {
        Booking booking = bookingRepository.findByTransaction(t);
        consumptionPushService.report(booking, t);

        DateTime startDateTime = t.getStartDateTime().toDateTime();
        availabilityPushService.arrivedAtPlace(
            stopTransactionDTO.getPedelecManufacturerId(),
            stopTransactionDTO.getStationManufacturerId(),
            startDateTime);

        placeAvailabilityPushService.reportChange(stopTransactionDTO.getStationManufacturerId());
    }

    public List<String> getAvailablePedelecs(String endpointAddress, String cardId) throws DatabaseException {

        if (cardId == null || cardId.isEmpty()) {
            return pedelecRepository.findAvailablePedelecs(endpointAddress);
        }

        List<String> pedelecs = pedelecRepository.findReservedPedelecs(endpointAddress, cardId);

        if (pedelecs.isEmpty()) {
            pedelecs = pedelecRepository.findAvailablePedelecs(endpointAddress);
        }

        return pedelecs;
    }

    public void handleStationStatusNotification(StationStatusDTO stationStatusDTO) {
        stationRepository.updateStationStatus(stationStatusDTO);
    }

    public void handlePedelecStatusNotification(PedelecStatusDTO pedelecStatusDTO) {
        pedelecRepository.updatePedelecStatus(pedelecStatusDTO);
    }

    public void handleChargingStatusNotification(List<ChargingStatusDTO> chargingStatusDTO) {
        pedelecRepository.updatePedelecChargingStatus(chargingStatusDTO);
    }
}
