package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.impl.PlaceAvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.ErrorType;
import xjc.schema.ixsi.PlaceAvailabilitySubscriptionStatusRequestType;
import xjc.schema.ixsi.PlaceAvailabilitySubscriptionStatusResponseType;
import xjc.schema.ixsi.ProviderPlaceIDType;

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
    public Class<PlaceAvailabilitySubscriptionStatusRequestType> getProcessingClass() {
        return PlaceAvailabilitySubscriptionStatusRequestType.class;
    }

    @Override
    public PlaceAvailabilitySubscriptionStatusResponseType process(PlaceAvailabilitySubscriptionStatusRequestType request, String systemId) {
        List<String> subscriptions = placeAvailabilityStore.getSubscriptions(systemId);

        List<ProviderPlaceIDType> ids = new ArrayList<>();
        for (String s : subscriptions) {
            ids.add(new ProviderPlaceIDType()
                .withPlaceID(s)
                .withProviderID(IXSIConstants.Provider.id)
            );
        }

        return new PlaceAvailabilitySubscriptionStatusResponseType().withPlaceID(ids);
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public PlaceAvailabilitySubscriptionStatusResponseType buildError(ErrorType e) {
        return new PlaceAvailabilitySubscriptionStatusResponseType().withError(e);
    }
}
