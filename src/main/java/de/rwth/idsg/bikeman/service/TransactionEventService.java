package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.Pedelec;
import de.rwth.idsg.bikeman.domain.StationSlot;
import de.rwth.idsg.bikeman.domain.TransactionEvent;
import de.rwth.idsg.bikeman.domain.TransactionType;
import de.rwth.idsg.bikeman.psinterface.dto.request.StartTransactionDTO;
import de.rwth.idsg.bikeman.psinterface.dto.request.StopTransactionDTO;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.PedelecRepository;
import de.rwth.idsg.bikeman.repository.StationSlotRepository;
import de.rwth.idsg.bikeman.repository.TransactionEventRepository;
import org.joda.time.LocalDateTime;
import org.springframework.scheduling.annotation.Async;
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

    @Async
    public void createAndSaveStartTransactionEvent(StartTransactionDTO startTransactionDTO) {

        CardAccount cardAccount = cardAccountRepository.findByCardId(startTransactionDTO.getCardId());
        Pedelec pedelec = pedelecRepository.findByManufacturerId(startTransactionDTO.getPedelecManufacturerId());
        StationSlot stationSlot = stationSlotRepository.findByManufacturerId(
                startTransactionDTO.getSlotManufacturerId(),
                startTransactionDTO.getStationManufacturerId());

        LocalDateTime arrivedTimestamp = startTransactionDTO.getTimestamp().toLocalDateTime();

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

    @Async
    public void createAndSaveStopTransactionEvent(StopTransactionDTO stopTransactionDTO) {

        Pedelec pedelec = pedelecRepository.findByManufacturerId(stopTransactionDTO.getPedelecManufacturerId());
        StationSlot stationSlot = stationSlotRepository.findByManufacturerId(
                stopTransactionDTO.getSlotManufacturerId(),
                stopTransactionDTO.getStationManufacturerId());

        LocalDateTime arrivedTimestamp = stopTransactionDTO.getTimestamp().toLocalDateTime();

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
