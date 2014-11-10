package de.rwth.idsg.bikeman.ixsi.processor;

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
public class PlaceAvailabilityStore extends AbstractSubscriptionStore {

    @PostConstruct
    public void init() {
        lookupTable = new ConcurrentHashMap<>();
        scheduler = Executors.newScheduledThreadPool(3);
    }

    @PreDestroy
    public void preDestroy() {
        // TODO Publish info about going down
    }

    @Override
    public void subscribe(String systemID, List<Long> itemIDs, Integer expireIntervalinMinutes) {
        // No op: PlaceAvailabilitySubscriptionRequestType defines no EventHorizon
    }
}
