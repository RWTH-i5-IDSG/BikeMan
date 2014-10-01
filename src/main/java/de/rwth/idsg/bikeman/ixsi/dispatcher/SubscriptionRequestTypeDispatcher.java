package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.*;
import de.rwth.idsg.bikeman.ixsi.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.09.2014
 */
@Slf4j
@Component
public class SubscriptionRequestTypeDispatcher extends AbstractRequestDispatcher {

    public SubscriptionRequestTypeDispatcher() {
        map = new HashMap<>();
        map.put(HeartBeatRequestType.class, new HeartBeatRequestProcessor());
        map.put(AvailabilitySubscriptionRequestType.class, new AvailabilitySubscriptionRequestProcessor());
        map.put(AvailabilitySubscriptionStatusRequest.class, new AvailabilitySubscriptionStatusRequestProcessor());
        map.put(PlaceAvailabilitySubscriptionRequestType.class, new PlaceAvailabilitySubscriptionRequestProcessor());
        map.put(PlaceAvailabilitySubscriptionStatusRequest.class, new PlaceAvailabilitySubscriptionStatusRequestProcessor());
        map.put(BookingAlertSubscriptionRequestType.class, new BookingAlertSubscriptionRequestProcessor());
        map.put(BookingAlertSubscriptionStatusRequestType.class, new BookingAlertSubscriptionStatusRequestProcessor());
        map.put(CompleteAvailabilityRequestType.class, new CompleteAvailabilityRequestProcessor());
        map.put(CompletePlaceAvailabilityRequestType.class, new CompletePlaceAvailabilityRequestProcessor());
        map.put(CompleteBookingAlertRequestType.class, new CompleteBookingAlertRequestProcessor());
        log.debug("Ready");
    }

    @Override
    public void handle(CommunicationContext context) {
        log.trace("Entered handle...");

        // There is only one SubscriptionRequestType in a IXSI message, hence the "get(0)"
        SubscriptionRequestType requestContainer = (SubscriptionRequestType) context.getIncomingIxsi().getMessageList().get(0);

        Object actualRequest = requestContainer.getSubscriptionRequest();
        Object actualResponse = super.delegate(actualRequest);

        // TODO process request, create response etc.
    }
}