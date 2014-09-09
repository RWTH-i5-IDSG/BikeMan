package de.rwth.idsg.velocity.psinterface.service;

import de.rwth.idsg.velocity.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.velocity.psinterface.dto.request.CustomerAuthorizeDTO;
import de.rwth.idsg.velocity.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.velocity.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.velocity.psinterface.dto.response.AuthorizeConfirmationDTO;
import de.rwth.idsg.velocity.psinterface.dto.response.BootConfirmationDTO;
import de.rwth.idsg.velocity.repository.CustomerRepository;
import de.rwth.idsg.velocity.repository.StationRepository;
import de.rwth.idsg.velocity.repository.TransactionRepository;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by swam on 04/08/14.
 */

@Service
@Slf4j
public class PedelecStationService {

    @Inject private CustomerRepository customerRepository;
    @Inject private TransactionRepository transactionRepository;
    @Inject private StationRepository stationRepository;

    private static final Integer HEARTBEAT_INTERVAL_IN_SECONDS = 60;

    public BootConfirmationDTO handleBootNotification(BootNotificationDTO bootNotificationDTO) throws DatabaseException {
        stationRepository.updateAfterBoot(bootNotificationDTO);

        BootConfirmationDTO bootConfirmationDTO = new BootConfirmationDTO();
        bootConfirmationDTO.setTimestamp(new Date().getTime());
        bootConfirmationDTO.setHeartbeatInterval(HEARTBEAT_INTERVAL_IN_SECONDS);
        return bootConfirmationDTO;
    }

    public AuthorizeConfirmationDTO handleAuthorize(CustomerAuthorizeDTO customerAuthorizeDTO) throws DatabaseException {
        long userId = customerRepository.findByCardIdAndCardPin(customerAuthorizeDTO.getCardId(), customerAuthorizeDTO.getPin());

        AuthorizeConfirmationDTO authorizeConfirmationDTO = new AuthorizeConfirmationDTO();
        authorizeConfirmationDTO.setUserId(userId);

        return authorizeConfirmationDTO;
    }

    public void handleStartTransaction(StartTransactionDTO startTransactionDTO) throws DatabaseException {
        transactionRepository.start(startTransactionDTO);
    }

    public void handleStopTransaction(StopTransactionDTO stopTransactionDTO) throws DatabaseException {
        transactionRepository.stop(stopTransactionDTO);
    }

}
