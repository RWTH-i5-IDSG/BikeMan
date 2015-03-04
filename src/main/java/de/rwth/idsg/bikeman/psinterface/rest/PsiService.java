package de.rwth.idsg.bikeman.psinterface.rest;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.service.AvailabilityPushService;
import de.rwth.idsg.bikeman.ixsi.service.ConsumptionPushService;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.CustomerAuthorizeDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.PedelecStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StationStatusDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.AuthorizeConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.AvailablePedelecDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.BootConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.exception.PsErrorCode;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.repository.CustomerRepository;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.repository.StationRepository;
import de.rwth.idsg.bikeman.repository.TransactionRepository;
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

    @Inject private CustomerRepository customerRepository;
    @Inject private TransactionRepository transactionRepository;
    @Inject private StationRepository stationRepository;
    @Inject private BookingRepository bookingRepository;
    @Inject private PedelecRepository pedelecRepository;

    @Inject private ConsumptionPushService consumptionPushService;
    @Inject private AvailabilityPushService availabilityPushService;

    private static final Integer HEARTBEAT_INTERVAL_IN_SECONDS = 60;

    public BootConfirmationDTO handleBootNotification(BootNotificationDTO bootNotificationDTO) throws DatabaseException {
        stationRepository.updateAfterBoot(bootNotificationDTO);
        
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
        transactionRepository.start(startTransactionDTO);
        availabilityPushService.takenFromPlace(
                startTransactionDTO.getPedelecManufacturerId(),
                startTransactionDTO.getStationManufacturerId(),
                new DateTime(startTransactionDTO.getTimestamp()));
    }

    public void handleStopTransaction(StopTransactionDTO stopTransactionDTO) throws DatabaseException {
        Transaction t = transactionRepository.stop(stopTransactionDTO);

        Optional<Long> optionalId = bookingRepository.findIdByTransaction(t);
        if (optionalId.isPresent()) {
            consumptionPushService.report(optionalId.get(), t);
        }

        DateTime startDateTime = t.getStartDateTime().toDateTime();
        availabilityPushService.arrivedAtPlace(
                stopTransactionDTO.getPedelecManufacturerId(),
                stopTransactionDTO.getStationManufacturerId(),
                startDateTime);
    }
    
    public List<AvailablePedelecDTO> getAvailablePedelecs(Long stationId) throws DatabaseException {
        return pedelecRepository.findAvailablePedelecs(stationId);
    }

    public Long getStationIdByEndpointAddress(String endpointAddress) throws DatabaseException {
        return stationRepository.getStationIdByEndpointAddress(endpointAddress);
    }

    public void handleStationStatusNotification(StationStatusDTO stationStatusDTO) {
        stationRepository.updateStationStatus(stationStatusDTO);
    }

    public void handlePedelecStatusNotification(PedelecStatusDTO pedelecStatusDTO) {
        pedelecRepository.updatePedelecStatus(pedelecStatusDTO);
    }
}
