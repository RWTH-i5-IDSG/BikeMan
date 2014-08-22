package de.rwth.idsg.velocity.psinterface.service;

import de.rwth.idsg.velocity.domain.Customer;
import de.rwth.idsg.velocity.domain.Transaction;
import de.rwth.idsg.velocity.psinterface.dto.request.BootNotificationDTO;
import de.rwth.idsg.velocity.psinterface.dto.request.CustomerAuthorizeDTO;
import de.rwth.idsg.velocity.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.velocity.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.velocity.psinterface.dto.response.AuthorizeConfirmationDTO;
import de.rwth.idsg.velocity.psinterface.dto.response.BootConfirmationDTO;
import de.rwth.idsg.velocity.repository.CustomerRepository;
import de.rwth.idsg.velocity.repository.TransactionRepository;
import de.rwth.idsg.velocity.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by swam on 04/08/14.
 */

@Service
@Transactional
@Slf4j
public class PedelecStationService {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private TransactionRepository transactionRepository;


    private static final Integer HEARTBEAT_INTERVAL_IN_SECONDS = 60;

    public BootConfirmationDTO handleBootNotification(BootNotificationDTO bootNotificationDTO) {

        // TODO: service to update all values from bootNotification

        BootConfirmationDTO bootConfirmationDTO = new BootConfirmationDTO();
        bootConfirmationDTO.setTimestamp(new Date().getTime());
        bootConfirmationDTO.setHeartbeatInterval(HEARTBEAT_INTERVAL_IN_SECONDS);

        return bootConfirmationDTO;
    }

    public AuthorizeConfirmationDTO handleAuthorize(CustomerAuthorizeDTO customerAuthorizeDTO) throws DatabaseException {
        String customerId = customerRepository.findByCardIdAndCardPin(customerAuthorizeDTO.getCardId(), customerAuthorizeDTO.getPin());

        AuthorizeConfirmationDTO authorizeConfirmationDTO = new AuthorizeConfirmationDTO();
        authorizeConfirmationDTO.setCustomerId(customerId);

        return authorizeConfirmationDTO;
    }

    public void handleStartTransaction(StartTransactionDTO startTransactionDTO) throws DatabaseException {
        transactionRepository.start(startTransactionDTO);
        // update pedelec, slot, user
    }

    public void handleStopTransaction(StopTransactionDTO stopTransactionDTO) throws DatabaseException {
        transactionRepository.stop(stopTransactionDTO);
        // update pedelec, slot, user
    }

}
