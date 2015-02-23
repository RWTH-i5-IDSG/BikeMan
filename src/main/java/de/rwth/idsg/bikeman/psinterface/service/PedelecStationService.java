package de.rwth.idsg.bikeman.psinterface.service;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.service.ConsumptionPushService;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.CustomerAuthorizeDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.AuthorizeConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.AvailablePedelecDTO;
import de.rwth.idsg.bikeman.psinterface.dto.response.BootConfirmationDTO;
import de.rwth.idsg.bikeman.psinterface.exception.PsException;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import de.rwth.idsg.bikeman.repository.CustomerRepository;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.repository.StationRepository;
import de.rwth.idsg.bikeman.repository.TransactionRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;

/**
 * Created by swam on 04/08/14.
 */

@Service
@Slf4j
public class PedelecStationService {

    @Inject private CustomerRepository customerRepository;
    @Inject private TransactionRepository transactionRepository;
    @Inject private StationRepository stationRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private ConsumptionPushService consumptionPushService;
    @Autowired private PedelecRepository pedelecRepository;

    private static final Integer HEARTBEAT_INTERVAL_IN_SECONDS = 60;

    public BootConfirmationDTO handleBootNotification(BootNotificationDTO bootNotificationDTO) throws PsException {
        stationRepository.updateAfterBoot(bootNotificationDTO);

        BootConfirmationDTO bootConfirmationDTO = new BootConfirmationDTO();
        bootConfirmationDTO.setTimestamp(Utils.getSecondsOfNow());
        bootConfirmationDTO.setHeartbeatInterval(HEARTBEAT_INTERVAL_IN_SECONDS);
        return bootConfirmationDTO;
    }

    public AuthorizeConfirmationDTO handleAuthorize(CustomerAuthorizeDTO customerAuthorizeDTO) throws DatabaseException {
        CardAccount cardAccount = customerRepository.findByCardIdAndCardPin(customerAuthorizeDTO.getCardId(), customerAuthorizeDTO.getPin());

        if (OperationState.INOPERATIVE.equals(cardAccount.getOperationState())) {
            throw new DatabaseException("Card is not operational!");
        }

        AuthorizeConfirmationDTO authorizeConfirmationDTO = new AuthorizeConfirmationDTO(cardAccount.getCardId());
        return authorizeConfirmationDTO;
    }

    public void handleStartTransaction(StartTransactionDTO startTransactionDTO) throws DatabaseException {
        transactionRepository.start(startTransactionDTO);
    }

    public void handleStopTransaction(StopTransactionDTO stopTransactionDTO) throws DatabaseException {
        Transaction t = transactionRepository.stop(stopTransactionDTO);

        Optional<Long> optionalId = bookingRepository.findIdByTransaction(t);
        if (optionalId.isPresent()) {
            consumptionPushService.report(optionalId.get(), t);
        }
    }
    
    public List<AvailablePedelecDTO> getAvailablePedelecs(Long stationId) throws DatabaseException{
        
        return pedelecRepository.findAvailablePedelecs(stationId);
    }
}
