package de.rwth.idsg.bikeman.ixsi.store;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Key : Station ID / PlaceID
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 04.11.2014
 */
@Service
public class PlaceAvailabilityStore extends AbstractSubscriptionStore<String> {

    @PostConstruct
    public void init() {
        log.trace("Ready");
    }

    @Override
    public void subscribe(String systemID, List<String> itemIDs, long expireIntervalinMinutes) {
        // No op: PlaceAvailabilitySubscriptionRequestType defines no EventHorizon
    }
}
