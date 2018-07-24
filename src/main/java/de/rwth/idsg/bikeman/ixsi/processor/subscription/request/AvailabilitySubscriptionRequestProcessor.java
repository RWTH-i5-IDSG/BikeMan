package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.store.AvailabilityStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.AvailabilitySubscriptionRequestType;
import xjc.schema.ixsi.AvailabilitySubscriptionResponseType;
import xjc.schema.ixsi.BookingTargetIDType;
import xjc.schema.ixsi.ErrorType;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Slf4j
@Component
public class AvailabilitySubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<AvailabilitySubscriptionRequestType, AvailabilitySubscriptionResponseType> {

    @Autowired private AvailabilityStore availabilityStore;

    @Override
    public Class<AvailabilitySubscriptionRequestType> getProcessingClass() {
        return AvailabilitySubscriptionRequestType.class;
    }

    @Override
    public AvailabilitySubscriptionResponseType process(AvailabilitySubscriptionRequestType request, String systemId) {

        List<BookingTargetIDType> itemIds = request.getBookingTargetID();

        if (request.isSetUnsubscription() && request.isUnsubscription()) {
            availabilityStore.unsubscribe(systemId, itemIds);

        } else if (request.isSetEventHorizon()) {
            try {
                long interval = request.getEventHorizon().toStandardMinutes().getMinutes();
                availabilityStore.subscribe(systemId, itemIds, interval);
            } catch (UnsupportedOperationException e) {
                log.error("Error occurred", e);
                return buildError(ErrorFactory.Sys.invalidRequest("EventHorizon with months and/or years are not supported since they vary in length", null));
            }
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
        return new AvailabilitySubscriptionResponseType().withError(e);
    }
}
