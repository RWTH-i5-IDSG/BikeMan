package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.impl.PlaceAvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionStatusRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionStatusResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ProviderPlaceIDType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class PlaceAvailabilitySubscriptionStatusRequestProcessor implements
        SubscriptionRequestProcessor<PlaceAvailabilitySubscriptionStatusRequestType, PlaceAvailabilitySubscriptionStatusResponseType> {

    @Autowired private PlaceAvailabilityStore placeAvailabilityStore;

    @Override
    public PlaceAvailabilitySubscriptionStatusResponseType process(PlaceAvailabilitySubscriptionStatusRequestType request, String systemId) {
        List<String> subscriptions = placeAvailabilityStore.getSubscriptions(systemId);

        List<ProviderPlaceIDType> ids = new ArrayList<>();
        for (String s : subscriptions) {
            ProviderPlaceIDType idType = new ProviderPlaceIDType();
            idType.setPlaceID(s);
            ids.add(idType);
        }

        PlaceAvailabilitySubscriptionStatusResponseType response = new PlaceAvailabilitySubscriptionStatusResponseType();
        response.getPlaceID().addAll(ids);
        return response;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public PlaceAvailabilitySubscriptionStatusResponseType buildError(ErrorType e) {
        PlaceAvailabilitySubscriptionStatusResponseType b = new PlaceAvailabilitySubscriptionStatusResponseType();
        b.getError().add(e);
        return b;
    }
}
