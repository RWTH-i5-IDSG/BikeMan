package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.impl.PlaceAvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.ErrorType;
import xjc.schema.ixsi.PlaceAvailabilitySubscriptionRequestType;
import xjc.schema.ixsi.PlaceAvailabilitySubscriptionResponseType;
import xjc.schema.ixsi.ProviderPlaceIDType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class PlaceAvailabilitySubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<PlaceAvailabilitySubscriptionRequestType, PlaceAvailabilitySubscriptionResponseType> {

    @Autowired
    private PlaceAvailabilityStore placeAvailabilityStore;

    @Override
    public PlaceAvailabilitySubscriptionResponseType process(PlaceAvailabilitySubscriptionRequestType request, String systemId) {

        List<String> itemIds = new ArrayList<>();
        for (ProviderPlaceIDType id : request.getPlaceID()) {
            itemIds.add(id.getPlaceID());
        }

        if (request.isSetUnsubscription() && request.isUnsubscription()) {
            placeAvailabilityStore.unsubscribe(systemId, itemIds);
        } else {
            placeAvailabilityStore.subscribe(systemId, itemIds);
        }

        return new PlaceAvailabilitySubscriptionResponseType();
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public PlaceAvailabilitySubscriptionResponseType buildError(ErrorType e) {
        return new PlaceAvailabilitySubscriptionResponseType().withError(e);
    }
}
