package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.processor.AvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetIDType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class AvailabilitySubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<AvailabilitySubscriptionRequestType, AvailabilitySubscriptionResponseType> {

    @Autowired private AvailabilityStore availabilityStore;

    @Override
    public AvailabilitySubscriptionResponseType process(AvailabilitySubscriptionRequestType request, String systemId) {

        List<String> itemIds = new ArrayList<>();
        for (BookingTargetIDType id : request.getBookingTargetID()) {
            itemIds.add(id.getBookeeID());
        }

        if (request.isSetUnsubscription() && request.isUnsubscription()) {
            availabilityStore.unsubscribe(systemId, itemIds);

        } else if (request.isSetEventHorizon()) {
            Integer interval = request.getEventHorizon().getMinutes();
            availabilityStore.subscribe(systemId, itemIds, interval);

        } else {
            availabilityStore.subscribe(systemId, itemIds);
        }

        return new AvailabilitySubscriptionResponseType();
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public AvailabilitySubscriptionResponseType buildError(ErrorType e) {
        AvailabilitySubscriptionResponseType b = new AvailabilitySubscriptionResponseType();
        b.getError().add(e);
        return b;
    }
}
