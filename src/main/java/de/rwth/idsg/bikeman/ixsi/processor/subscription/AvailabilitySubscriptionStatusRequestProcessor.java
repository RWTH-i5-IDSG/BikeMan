package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.AvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionStatusRequest;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionStatusResponse;
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
public class AvailabilitySubscriptionStatusRequestProcessor implements
        SubscriptionRequestProcessor<AvailabilitySubscriptionStatusRequest, AvailabilitySubscriptionStatusResponse> {

    @Autowired private AvailabilityStore availabilityStore;

    @Override
    public AvailabilitySubscriptionStatusResponse process(AvailabilitySubscriptionStatusRequest request, String systemId) {
        List<Long> subscriptions = availabilityStore.getSubscriptions(systemId);

        List<BookingTargetIDType> ids = new ArrayList<>();
        for (Long s : subscriptions) {
            BookingTargetIDType idType = new BookingTargetIDType();
            idType.setBookeeID(s.toString());
            ids.add(idType);
        }

        AvailabilitySubscriptionStatusResponse response = new AvailabilitySubscriptionStatusResponse();
        response.getBookingTargetID().addAll(ids);
        return response;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public AvailabilitySubscriptionStatusResponse invalidSystem() {
        AvailabilitySubscriptionStatusResponse b = new AvailabilitySubscriptionStatusResponse();
        b.getError().add(ErrorFactory.invalidSystem());
        return b;
    }
}
