package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.AvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetIDType;
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

    @Autowired AvailabilityStore availabilityStore;

    @Override
    public AvailabilitySubscriptionResponseType process(AvailabilitySubscriptionRequestType request, String systemId) {
        // transform bookingTargetIds
        List<Long> itemIds = new ArrayList<>();
        for (BookingTargetIDType id : request.getBookingTargetID()) {
            try {
                itemIds.add(Long.valueOf(id.getBookeeID()));
            } catch (NumberFormatException e) {
                return invalidSystem();
            }
        }

        if (request.isUnsubscription()) {
            availabilityStore.unsubscribe(systemId, itemIds);
        } else {
            if (request.isSetEventHorizon()) {
                availabilityStore.subscribe(systemId, itemIds);
            } else {
                Integer interval = request.getEventHorizon().getMinutes();
                availabilityStore.subscribe(systemId, itemIds, interval);
            }
        }

        return new AvailabilitySubscriptionResponseType();
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public AvailabilitySubscriptionResponseType invalidSystem() {
        AvailabilitySubscriptionResponseType b = new AvailabilitySubscriptionResponseType();
        b.getError().add(ErrorFactory.invalidSystem());
        return b;
    }
}