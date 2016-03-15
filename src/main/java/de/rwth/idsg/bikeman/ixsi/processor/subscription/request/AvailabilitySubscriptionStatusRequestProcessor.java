package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.store.AvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.AvailabilitySubscriptionStatusRequestType;
import xjc.schema.ixsi.AvailabilitySubscriptionStatusResponseType;
import xjc.schema.ixsi.BookingTargetIDType;
import xjc.schema.ixsi.ErrorType;

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
    public Class<AvailabilitySubscriptionStatusRequestType> getProcessingClass() {
        return AvailabilitySubscriptionStatusRequestType.class;
    }

    @Override
    public AvailabilitySubscriptionStatusResponseType process(AvailabilitySubscriptionStatusRequestType request, String systemId) {
        List<BookingTargetIDType> subscriptions = availabilityStore.getSubscriptions(systemId);

        return new AvailabilitySubscriptionStatusResponseType().withBookingTargetID(subscriptions);
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public AvailabilitySubscriptionStatusResponseType buildError(ErrorType e) {
        return new AvailabilitySubscriptionStatusResponseType().withError(e);
    }
}
