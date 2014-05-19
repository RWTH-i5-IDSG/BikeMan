package de.rwth.idsg.velocity.service;

import de.rwth.idsg.velocity.domain.Customer;
import de.rwth.idsg.velocity.domain.Pedelec;
import de.rwth.idsg.velocity.domain.StationSlot;
import de.rwth.idsg.velocity.domain.Transaction;
import de.rwth.idsg.velocity.endpoint.StationEndpoint;
import de.rwth.idsg.velocity.repository.PedelecRepository;
import de.rwth.idsg.velocity.repository.StationSlotRepository;
import de.rwth.idsg.velocity.repository.TransactionRepository;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class RentalService {

    @Inject
    private StationSlotRepository stationSlotRepository;

    @Inject
    private TransactionRepository transactionRepository;

    @Inject
    private PedelecRepository pedelecRepository;

    @Inject
    private UserService userService;

    private static int STATION_TIMEOUT_SEC = 10;

    private final Logger log = LoggerFactory.getLogger(RentalService.class);

    private ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(1);

    private Transaction tmpTransaction;

    public boolean pickUp(final long stationSlotId) {

        final StationSlot stationSlot = stationSlotRepository.findOne(stationSlotId);
        final Pedelec pedelec = stationSlot.getPedelec();
        final Customer currentUser = (Customer)userService.getUserWithAuthorities();
//
//        // when stationslot, pedelec is disabled or currentUser not activated, stop pickup process
        if (!checkComponents(stationSlot, pedelec, currentUser)) {
            // create error msg
            return false;
        }

        // Create connection to Station
        final StationEndpoint stationEndpoint = new StationEndpoint(stationSlot.getStation());

        // Request Station to open slot for pedelec removal
        if (stationEndpoint.openSlot(stationSlot)) {
            // if slot is opened, start timer
            scheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {

                    if (stationEndpoint.slotIsEngadged(stationSlot)) {
                        // pedelec is still in slot >> close slot.
                    } else {
                        createTransaction(stationSlot, pedelec, currentUser);
                        stationSlot.setPedelec(null);
                        stationSlotRepository.save(stationSlot);
                    }

                }
            }, STATION_TIMEOUT_SEC, TimeUnit.SECONDS);

            return true;

        } else {
            return false;
        }

    }

    private boolean checkComponents(StationSlot stationSlot, Pedelec pedelec, Customer currentUser) {
        if (stationSlot.getState() && pedelec != null && pedelec.getState() && currentUser.getIsActivated()) {
            return true;
        }
        return false;
    }

    private void createTransaction(StationSlot slot, Pedelec pedelec, Customer currentUser) {

        Transaction transaction = new Transaction();
        transaction.setPedelec(pedelec);
        transaction.setFromSlot(slot);
        transaction.setCustomer(currentUser);
        transaction.setStartDateTime(new LocalDateTime().now());

        transactionRepository.save(transaction);
    }


    public boolean returnPedelec(long pedelecId, long slotId) {

        Pedelec pedelec = pedelecRepository.findOne(pedelecId);
        StationSlot stationSlot = stationSlotRepository.findOne(slotId);

        stationSlot.setPedelec(pedelec);
        stationSlotRepository.save(stationSlot);

        // Get last transaction for pedelec with pedelec id
        Transaction activeTransaction = transactionRepository.findLastTransactionByPedelecId(pedelec);

        // add end slot and end time to transaction
        activeTransaction.setEndDateTime(new LocalDateTime().now());
        activeTransaction.setToSlot(stationSlot);

        // return transaction
        transactionRepository.save(activeTransaction);

        return false;
    }
}
