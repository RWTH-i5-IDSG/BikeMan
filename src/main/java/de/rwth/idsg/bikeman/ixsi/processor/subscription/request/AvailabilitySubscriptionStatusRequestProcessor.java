package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.impl.AvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionStatusRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionStatusResponseType;
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
public class AvailabilitySubscriptionStatusRequestProcessor implements
        SubscriptionRequestProcessor<AvailabilitySubscriptionStatusRequestType, AvailabilitySubscriptionStatusResponseType> {

    @Autowired private AvailabilityStore availabilityStore;

    @Override
    public AvailabilitySubscriptionStatusResponseType process(AvailabilitySubscriptionStatusRequestType request, String systemId) {
        List<String> subscriptions = availabilityStore.getSubscriptions(systemId);

        List<BookingTargetIDType> ids = new ArrayList<>();
        for (String s : subscriptions) {
            BookingTargetIDType idType = new BookingTargetIDType();
            idType.setBookeeID(s);
            ids.add(idType);
        }

        AvailabilitySubscriptionStatusResponseType response = new AvailabilitySubscriptionStatusResponseType();
        response.getBookingTargetID().addAll(ids);
        return response;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public AvailabilitySubscriptionStatusResponseType buildError(ErrorType e) {
        AvailabilitySubscriptionStatusResponseType b = new AvailabilitySubscriptionStatusResponseType();
        b.getError().add(e);
        return b;
    }
}
