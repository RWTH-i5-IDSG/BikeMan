package de.rwth.idsg.bikeman.ixsi.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * Key : Booking ID
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 08.01.2015
 */
@Slf4j
@Service
public class ConsumptionStore extends AbstractSubscriptionStore<String> {

    @PostConstruct
    public void init() {
        lookupTable = new ConcurrentHashMap<>();
        scheduler = Executors.newScheduledThreadPool(3, threadFactory);
    }

    @PreDestroy
    public void preDestroy() {
        log.debug("ConsumptionStore is being destroyed");
        scheduler.shutdown();
        // TODO Publish info about going down
    }

}
