package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.PlaceAvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionStatusRequest;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionStatusResponse;
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
        SubscriptionRequestProcessor<PlaceAvailabilitySubscriptionStatusRequest, PlaceAvailabilitySubscriptionStatusResponse> {

    @Autowired PlaceAvailabilityStore placeAvailabilityStore;

    @Override
    public PlaceAvailabilitySubscriptionStatusResponse process(PlaceAvailabilitySubscriptionStatusRequest request, String systemId) {
        List<Long> subscriptions = placeAvailabilityStore.getSubscriptions(systemId);

        PlaceAvailabilitySubscriptionStatusResponse response = new PlaceAvailabilitySubscriptionStatusResponse();
        List<ProviderPlaceIDType> ids = new ArrayList<>();
        for (Long s : subscriptions) {
            ProviderPlaceIDType idType = new ProviderPlaceIDType();
            idType.setPlaceID(s.toString());
            ids.add(idType);
        }
        response.getPlaceID().addAll(ids);

        return response;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public PlaceAvailabilitySubscriptionStatusResponse invalidSystem() {
        PlaceAvailabilitySubscriptionStatusResponse b = new PlaceAvailabilitySubscriptionStatusResponse();
        b.getError().add(ErrorFactory.invalidSystem());
        return b;
    }
}