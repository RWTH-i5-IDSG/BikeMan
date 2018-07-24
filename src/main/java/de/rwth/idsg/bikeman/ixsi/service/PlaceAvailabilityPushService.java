package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.endpoint.Producer;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.store.PlaceAvailabilityStore;
import de.rwth.idsg.bikeman.repository.StationRepository;
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

    @Autowired private Producer producer;
    @Autowired private PlaceAvailabilityStore placeAvailabilityStore;
    @Autowired private QueryIXSIRepository queryIXSIRepository;
    @Autowired private StationRepository stationRepository;

    public void reportChange(String placeID) {
        Integer freeSlots = getFreeSlots(placeID);
        reportChange(placeID, freeSlots);
    }

    private void reportChange(String placeID, int freeSlots) {
        Set<String> systemIdSet = placeAvailabilityStore.getSubscribedSystems(placeID);
        if (systemIdSet.isEmpty()) {
            log.debug("Will not push. There is no subscribed system for placeID '{}'", placeID);
            return;
        }

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

    /**
     * If the station is inoperative/deleted, the place availability based on slot state does not matter.
     */
    private Integer getFreeSlots(String placeID) {
        Station station = stationRepository.findByManufacturerId(placeID);
        switch (station.getState()) {
            case OPERATIVE:
                return queryIXSIRepository.placeAvailability(Arrays.asList(placeID))
                                          .get(0)
                                          .getAvailableSlots();
            case INOPERATIVE:
            case DELETED:
                return 0;
            default:
                throw new RuntimeException("Unexpected state");
        }
    }
}
