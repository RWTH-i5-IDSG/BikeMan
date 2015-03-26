package de.rwth.idsg.bikeman.psinterface.rest;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.impl.ExternalBookingStore;
import de.rwth.idsg.bikeman.ixsi.service.AvailabilityPushService;
import de.rwth.idsg.bikeman.ixsi.service.ConsumptionPushService;
import de.rwth.idsg.bikeman.ixsi.service.ExternalBookingPushService;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.*;
import de.rwth.idsg.bikeman.psinterface.dto.response.AuthorizeConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.AvailablePedelecDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.BootConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.repository.*;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by swam on 04/08/14.
 */

@Service
@Slf4j
public class PsiService {

    @Inject
    private CustomerRepository customerRepository;
    @Inject
    private TransactionRepository transactionRepository;
    @Inject
    private StationRepository stationRepository;
    @Inject
    private BookingRepository bookingRepository;
    @Inject
    private PedelecRepository pedelecRepository;
    @Inject
    private CardAccountRepository cardAccountRepository;
    @Inject
    private MajorCustomerRepository majorCustomerRepository;

    @Inject
    private ConsumptionPushService consumptionPushService;
    @Inject
    private AvailabilityPushService availabilityPushService;
    @Inject
    private ExternalBookingPushService externalBookingPushService;

    @Inject
    private ExternalBookingStore externalBookingStore;

    private static final Integer HEARTBEAT_INTERVAL_IN_SECONDS = 60;

    public BootConfirmationDTO handleBootNotification(BootNotificationDTO bootNotificationDTO,
                                                      String endpointAddress) throws DatabaseException {

        stationRepository.updateAfterBoot(bootNotificationDTO, endpointAddress);

        BootConfirmationDTO bootConfirmationDTO = new BootConfirmationDTO();
        bootConfirmationDTO.setTimestamp(Utils.nowInSeconds());
        bootConfirmationDTO.setHeartbeatInterval(HEARTBEAT_INTERVAL_IN_SECONDS);
        return bootConfirmationDTO;
    }

    public AuthorizeConfirmationDTO handleAuthorize(CustomerAuthorizeDTO customerAuthorizeDTO) throws DatabaseException {
        CardAccount cardAccount = customerRepository.findByCardIdAndCardPin(customerAuthorizeDTO.getCardId(), customerAuthorizeDTO.getCardPin());

        if (OperationState.INOPERATIVE.equals(cardAccount.getOperationState())) {
            throw new PsException("Card is not operational!", PsErrorCode.CONSTRAINT_FAILED);
        }

        return new AuthorizeConfirmationDTO(cardAccount.getCardId());
    }

    public void handleStartTransaction(StartTransactionDTO startTransactionDTO) throws DatabaseException {
        Transaction t = transactionRepository.start(startTransactionDTO);

        Long bookingId = bookingRepository.findIdByTransaction(t);
        externalBookingPushService.report(bookingId, t);

        availabilityPushService.takenFromPlace(
            startTransactionDTO.getPedelecManufacturerId(),
            startTransactionDTO.getStationManufacturerId(),
            new DateTime(startTransactionDTO.getTimestamp()));
    }

    public void handleStopTransaction(StopTransactionDTO stopTransactionDTO) throws DatabaseException {
        Transaction t = transactionRepository.stop(stopTransactionDTO);

        Long bookingId = bookingRepository.findIdByTransaction(t);
        consumptionPushService.report(bookingId, t);

        DateTime startDateTime = t.getStartDateTime().toDateTime();
        availabilityPushService.arrivedAtPlace(
            stopTransactionDTO.getPedelecManufacturerId(),
            stopTransactionDTO.getStationManufacturerId(),
            startDateTime);
    }

    public List<AvailablePedelecDTO> getAvailablePedelecs(String endpointAddress) throws DatabaseException {
        return pedelecRepository.findAvailablePedelecs(endpointAddress);
    }

    public void handleStationStatusNotification(StationStatusDTO stationStatusDTO) {
        stationRepository.updateStationStatus(stationStatusDTO);
    }

    public void handlePedelecStatusNotification(PedelecStatusDTO pedelecStatusDTO) {
        pedelecRepository.updatePedelecStatus(pedelecStatusDTO);
    }
}
