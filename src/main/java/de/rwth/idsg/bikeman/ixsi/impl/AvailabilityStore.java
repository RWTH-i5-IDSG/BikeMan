package de.rwth.idsg.bikeman.ixsi.impl;

import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetIDType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * Key : Pedelec ID / BookingTargetID
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 04.11.2014
 */
@Slf4j
@Service
public class AvailabilityStore extends AbstractSubscriptionStore<BookingTargetIDType> {

    @PostConstruct
    public void init() {
        lookupTable = new ConcurrentHashMap<>();
        scheduler = Executors.newScheduledThreadPool(3, threadFactory);
    }

    @PreDestroy
    public void preDestroy() {
        log.debug("AvailabilityStore is being destroyed");
        scheduler.shutdown();
        // TODO Publish info about going down
    }

}
