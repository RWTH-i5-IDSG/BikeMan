package de.rwth.idsg.bikeman.psinterface.rest;

import com.google.common.base.Strings;
import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Reservation;
import de.rwth.idsg.bikeman.domain.ReservationState;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.service.AvailabilityPushService;
import de.rwth.idsg.bikeman.ixsi.service.ConsumptionPushService;
import de.rwth.idsg.bikeman.ixsi.service.ExternalBookingPushService;
import de.rwth.idsg.bikeman.ixsi.service.PlaceAvailabilityPushService;
import de.rwth.idsg.bikeman.psinterface.Utils;
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
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.psinterface.repository.PsiBookingRepository;
import de.rwth.idsg.bikeman.psinterface.repository.PsiCustomerRepository;
import de.rwth.idsg.bikeman.psinterface.repository.PsiPedelecRepository;
import de.rwth.idsg.bikeman.psinterface.repository.PsiReservationRepository;
import de.rwth.idsg.bikeman.psinterface.repository.PsiStationRepository;
import de.rwth.idsg.bikeman.psinterface.repository.PsiTransactionRepository;
import de.rwth.idsg.bikeman.service.OperationStateService;
import de.rwth.idsg.bikeman.service.TransactionEventService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode.AUTH_ATTEMPTS_EXCEEDED;
import static de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode.CONSTRAINT_FAILED;

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
    @Inject private OperationStateService operationStateService;

    private static final Integer HEARTBEAT_INTERVAL_IN_SECONDS = 60;
    private static final int MAX_AUTH_RETRIES = 3;

    public BootConfirmationDTO handleBootNotification(BootNotificationDTO bootNotificationDTO)
            throws DatabaseException {

        stationRepository.updateAfterBoot(bootNotificationDTO);
        List<CardKeyDTO> cardKeys = stationRepository.getCardKeys();

        BootConfirmationDTO bootConfirmationDTO = new BootConfirmationDTO();
        bootConfirmationDTO.setTimestamp(Utils.nowInSeconds());
        bootConfirmationDTO.setHeartbeatInterval(HEARTBEAT_INTERVAL_IN_SECONDS);
        bootConfirmationDTO.setCardKeys(cardKeys);
        return bootConfirmationDTO;
    }

    @Transactional
    public AuthorizeConfirmationDTO handleAuthorize(CustomerAuthorizeDTO customerAuthorizeDTO)
            throws DatabaseException {

        log.info("Card with CardId {} start authorization from Station", customerAuthorizeDTO.getCardId());

        CardAccount cardAccount = customerRepository.findByCardId(customerAuthorizeDTO.getCardId());

        checkOperationState(cardAccount, customerAuthorizeDTO);
        checkPin(cardAccount, customerAuthorizeDTO);

        int actualRentedPedelecs = transactionRepository.countOpenTransactions(cardAccount.getCardId());
        int canRentCount = cardAccount.getCurrentTariff().getTariff().getMaxNumberPedelecs();

        return new AuthorizeConfirmationDTO(cardAccount.getCardId(), actualRentedPedelecs, canRentCount);
    }

    @Transactional
    public void handleStartTransaction(StartTransactionDTO startTransactionDTO) throws DatabaseException {
        transactionEventService.createAndSaveStartTransactionEvent(startTransactionDTO);

        Transaction transaction = transactionRepository.start(startTransactionDTO);

        List<Reservation> reservationList = reservationRepository.find(transaction.getCardAccount().getCardAccountId(),
                transaction.getPedelec().getPedelecId(),
                LocalDateTime.now());

        Booking booking;

        if (reservationList == null || reservationList.isEmpty()) {
            log.debug("No reservation found for startTransaction.");
            booking = new Booking();

        } else if (reservationList.size() == 1) {
            Reservation res = reservationList.get(0);
            reservationRepository.updateState(res, ReservationState.USED);
            booking = bookingRepository.findByReservation(res);
            log.debug("Found Reservation: {}", res);

        } else {
            throw new PsException("More than one reservation found", CONSTRAINT_FAILED);
        }

        booking.setTransaction(transaction);
        bookingRepository.save(booking);

        if (reservationList == null || reservationList.isEmpty()) {
            performExternalBookingPush(booking, transaction);
            log.debug("Perform External Booking for booking: {} and Transaction: {}", booking, transaction);
        }

        performStartPush(startTransactionDTO, transaction, booking);
        log.debug("Perform Start Push for Transaction: {} and Booking: {}", transaction, booking);
    }

    @Async
    private void performExternalBookingPush(Booking booking, Transaction transaction) {
        externalBookingPushService.report(booking, transaction);
    }

    @Async
    private void performStartPush(StartTransactionDTO startTransactionDTO, Transaction t, Booking booking) {
//        externalBookingPushService.report(booking, t);

        Long timestampInMillis = Utils.toMillis(t.getStartDateTime());

        availabilityPushService.takenFromPlace(
                startTransactionDTO.getPedelecManufacturerId(),
                new DateTime(timestampInMillis));

        placeAvailabilityPushService.reportChange(startTransactionDTO.getStationManufacturerId());
    }

    @Transactional
    public void handleStopTransaction(StopTransactionDTO stopTransactionDTO) throws DatabaseException {
        transactionEventService.createAndSaveStopTransactionEvent(stopTransactionDTO);

        Transaction t = transactionRepository.stop(stopTransactionDTO);

        if (t != null) {
            performStopPush(stopTransactionDTO, t);
        }
    }

    @Async
    private void performStopPush(StopTransactionDTO stopTransactionDTO, Transaction t) {
        Booking booking = bookingRepository.findByTransaction(t);
        consumptionPushService.report(booking);

        DateTime startDateTime = t.getStartDateTime().toDateTime();
        availabilityPushService.arrivedAtPlace(
                stopTransactionDTO.getPedelecManufacturerId(),
                stopTransactionDTO.getStationManufacturerId(),
                startDateTime);

        placeAvailabilityPushService.reportChange(stopTransactionDTO.getStationManufacturerId());
    }

    public List<String> getAvailablePedelecs(String stationManufacturerId, String cardId) throws DatabaseException {
        if (Strings.isNullOrEmpty(cardId)) {
            log.debug("cardId is not set. Returning available pedelecs");
            return pedelecRepository.findAvailablePedelecs(stationManufacturerId);
        }

        log.debug("Querying reserved pedelecs for cardId '{}'", cardId);
        List<String> pedelecs = pedelecRepository.findReservedPedelecs(stationManufacturerId, cardId);

        if (pedelecs.isEmpty()) {
            log.debug("cardId '{}' has no reservations. Returning available pedelecs", cardId);
            pedelecs = pedelecRepository.findAvailablePedelecs(stationManufacturerId);
        }

        return pedelecs;
    }

    public void handleStationStatusNotification(StationStatusDTO stationStatusDTO) {
        operationStateService.pushAvailability(stationStatusDTO);
        operationStateService.pushInavailability(stationStatusDTO);

        stationRepository.updateStationStatus(stationStatusDTO);
    }

    public void handlePedelecStatusNotification(PedelecStatusDTO pedelecStatusDTO) {
        operationStateService.pushAvailability(pedelecStatusDTO);
        operationStateService.pushInavailability(pedelecStatusDTO);

        pedelecRepository.updatePedelecStatus(pedelecStatusDTO);
    }

    public void handleChargingStatusNotification(List<ChargingStatusDTO> chargingStatusDTO) {
        pedelecRepository.updatePedelecChargingStatus(chargingStatusDTO);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private void checkOperationState(CardAccount ca, CustomerAuthorizeDTO dto) {
        if (OperationState.OPERATIVE.equals(ca.getOperationState())) {
            return;
        }

        log.info("Card with CardId {} authorization failed with {}", dto.getCardId(), CONSTRAINT_FAILED);
        throw new PsException("Card account is disabled", CONSTRAINT_FAILED);
    }

    private void checkPin(CardAccount ca, CustomerAuthorizeDTO dto) {
        if (ca.getCardPin().equals(dto.getCardPin())) {
            // PIN is correct, reset auth trial count
            customerRepository.resetAuthenticationTrialCount(ca);
            return;
        }

        // increase auth fail count by one
        ca.setAuthenticationTrialCount(ca.getAuthenticationTrialCount() + 1);

        try {
            checkPinRetryLimit(ca);
        } finally {
            customerRepository.saveCardAccount(ca);
        }

        log.info("Card with CardId {} authorization failed (wrong pin) with {}", dto.getCardId(), CONSTRAINT_FAILED);
        throw new PsException("Wrong PIN", CONSTRAINT_FAILED);
    }

    private void checkPinRetryLimit(CardAccount ca) {
        boolean exceeded = ca.getAuthenticationTrialCount() >= MAX_AUTH_RETRIES;

        // auth attempts exceeded
        if (exceeded) {
            ca.setOperationState(OperationState.INOPERATIVE);

            log.warn("Card with CardId {} authorization failed (3x wrong pin) with {}", ca.getCardId(), AUTH_ATTEMPTS_EXCEEDED);
            throw new PsException("No trials remaining and account gets disabled", AUTH_ATTEMPTS_EXCEEDED);
        }
    }
}
