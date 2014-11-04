package de.rwth.idsg.bikeman.ixsi.processor;

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
public class AvailabilityStore extends AbstractSubscriptionStore {

    @PostConstruct
    public void init() {
        lookupTable = new ConcurrentHashMap<>();
        scheduler = Executors.newScheduledThreadPool(3);
    }

    @PreDestroy
    public void preDestroy() {
        // TODO Publish info about going down
    }

}
