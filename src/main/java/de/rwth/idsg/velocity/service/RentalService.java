package de.rwth.idsg.velocity.service;

import de.rwth.idsg.velocity.domain.*;
import de.rwth.idsg.velocity.repository.StationSlotRepository;
import de.rwth.idsg.velocity.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;
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
    private UserService userService;

    private final Logger log = LoggerFactory.getLogger(RentalService.class);

    private ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(1);

    private Transaction tmpTransaction;

    public boolean pickUp(final long stationSlotId) {

//        StationSlot stationSlot = stationSlotRepository.findOne(stationSlotId);
//        Pedelec pedelec = stationSlot.getPedelec();
//        Customer currentUser = (Customer)userService.getUserWithAuthorities();
//
//        // when stationslot, pedelec is disabled or currentUser not activated, stop pickup process
//        if (!stationSlot.getState() || pedelec != null || !pedelec.getState() || !currentUser.getIsActivated()) {
//            return false;
//        }

        // release bike at station


        // when station says: ok, return true

        // create tmp transaction
//        tmpTransaction = new Transaction();
//        tmpTransaction.setCustomer(currentUser);
//        tmpTransaction.setFromSlot(stationSlot);
//        tmpTransaction.setPedelec(pedelec);

        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                log.debug("{}", stationSlotId);
            }
        }, 10, TimeUnit.SECONDS);



        return true;
    }


    public boolean returnPedelec(long bikeSlotId) {


        return false;
    }
}
