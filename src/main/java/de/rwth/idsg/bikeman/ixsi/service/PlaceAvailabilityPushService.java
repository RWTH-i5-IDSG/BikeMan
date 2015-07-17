package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import de.rwth.idsg.bikeman.ixsi.impl.PlaceAvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xjc.schema.ixsi.IxsiMessageType;
import xjc.schema.ixsi.PlaceAvailabilityPushMessageType;
import xjc.schema.ixsi.PlaceAvailabilityType;
import xjc.schema.ixsi.ProviderPlaceIDType;
import xjc.schema.ixsi.SubscriptionMessageType;

import java.util.Arrays;
import java.util.Set;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.11.2014
 */
@Slf4j
@Service
public class PlaceAvailabilityPushService {

    @Autowired
    private Producer producer;
    @Autowired
    private PlaceAvailabilityStore placeAvailabilityStore;
    @Autowired
    private QueryIXSIRepository queryIXSIRepository;

    public void reportChange(String placeID) {
        Set<String> systemIdSet = placeAvailabilityStore.getSubscribedSystems(placeID);
        if (systemIdSet.isEmpty()) {
            log.debug("Will not push. There is no subscribed system for placeID '{}'", placeID);
            return;
        }

        Integer freeSlots = queryIXSIRepository.placeAvailability(Arrays.asList(placeID))
                                               .get(0)
                                               .getAvailableSlots();

        ProviderPlaceIDType placeIDType = new ProviderPlaceIDType()
                .withPlaceID(placeID)
                .withProviderID(IXSIConstants.Provider.id);

        PlaceAvailabilityType avail = new PlaceAvailabilityType()
                .withAvailability(freeSlots)
                .withID(placeIDType);

        PlaceAvailabilityPushMessageType push = new PlaceAvailabilityPushMessageType().withPlaceAvailability(avail);
        SubscriptionMessageType sub = new SubscriptionMessageType().withPushMessageGroup(push);
        IxsiMessageType ixsi = new IxsiMessageType().withSubscriptionMessage(sub);

        producer.send(ixsi, systemIdSet);
    }

}
