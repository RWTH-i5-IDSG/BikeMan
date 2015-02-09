package de.rwth.idsg.bikeman.ixsi.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * Key : Station ID / PlaceID
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 04.11.2014
 */
@Slf4j
@Service
public class PlaceAvailabilityStore extends AbstractSubscriptionStore<String> {

    @PostConstruct
    public void init() {
        lookupTable = new ConcurrentHashMap<>();
        scheduler = Executors.newScheduledThreadPool(3, threadFactory);
    }

    @PreDestroy
    public void preDestroy() {
        log.debug("PlaceAvailabilityStore is being destroyed");
        scheduler.shutdown();
        // TODO Publish info about going down
    }

    @Override
    public void subscribe(String systemID, List<String> itemIDs, Integer expireIntervalinMinutes) {
        // No op: PlaceAvailabilitySubscriptionRequestType defines no EventHorizon
    }
}
