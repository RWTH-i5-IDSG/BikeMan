package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.psinterface.Utils;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.repository.StationSlotRepository;
import de.rwth.idsg.bikeman.repository.TransactionEventRepository;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by swam on 01/07/15.
 */

@Service
@Transactional
public class TransactionEventService {

    @Inject private CardAccountRepository cardAccountRepository;
    @Inject private PedelecRepository pedelecRepository;
    @Inject private StationSlotRepository stationSlotRepository;

    @Inject private TransactionEventRepository transactionEventRepository;

    public void createAndSaveStartTransactionEvent(StartTransactionDTO startTransactionDTO) {

        CardAccount cardAccount = cardAccountRepository.findByCardId(startTransactionDTO.getCardId());
        Pedelec pedelec = pedelecRepository.findByManufacturerId(startTransactionDTO.getPedelecManufacturerId());
        StationSlot stationSlot = stationSlotRepository.findByManufacturerId(startTransactionDTO.getSlotManufacturerId());

        Long timestampInMillis = Utils.toMillis(startTransactionDTO.getTimestamp());
        LocalDateTime arrivedTimestamp = new LocalDateTime(timestampInMillis);

        TransactionEvent transactionEvent = new TransactionEvent();

        transactionEvent.setCardAccount(cardAccount);
        transactionEvent.setStationSlot(stationSlot);
        transactionEvent.setArrivedTimestamp(arrivedTimestamp);
        transactionEvent.setTimestamp(LocalDateTime.now());
        transactionEvent.setPedelec(pedelec);
        transactionEvent.setStatus(null);
        transactionEvent.setType(TransactionType.START);

        transactionEventRepository.save(transactionEvent);
    }

    public void createAndSaveStopTransactionEvent(StopTransactionDTO stopTransactionDTO) {

        Pedelec pedelec = pedelecRepository.findByManufacturerId(stopTransactionDTO.getPedelecManufacturerId());
        StationSlot stationSlot = stationSlotRepository.findByManufacturerId(stopTransactionDTO.getSlotManufacturerId());

        Long timestampInMillis = Utils.toMillis(stopTransactionDTO.getTimestamp());
        LocalDateTime arrivedTimestamp = new LocalDateTime(timestampInMillis);

        TransactionEvent transactionEvent = new TransactionEvent();

        transactionEvent.setPedelec(pedelec);
        transactionEvent.setStationSlot(stationSlot);
        transactionEvent.setArrivedTimestamp(arrivedTimestamp);
        transactionEvent.setTimestamp(LocalDateTime.now());
        transactionEvent.setStatus(null);
        transactionEvent.setType(TransactionType.STOP);

        transactionEventRepository.save(transactionEvent);
    }

}
