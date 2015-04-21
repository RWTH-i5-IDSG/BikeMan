package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestMessageProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.complete.CompleteAvailabilityRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.complete.CompleteBookingAlertRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.complete.CompleteConsumptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.complete.CompleteExternalBookingRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.complete.CompletePlaceAvailabilityRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteBookingAlertRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteConsumptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteExternalBookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompletePlaceAvailabilityRequestType;
import de.rwth.idsg.ixsi.jaxb.RequestMessageGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 21.10.2014
 */
@Slf4j
@Component
public class SubscriptionRequestMessageMap extends HashMap<Class<?>, SubscriptionRequestMessageProcessor> {
    private static final long serialVersionUID = -3926494758975242383L;

    @Autowired private CompleteAvailabilityRequestProcessor completeAvailabilityRequestProcessor;
    @Autowired private CompletePlaceAvailabilityRequestProcessor completePlaceAvailabilityRequestProcessor;
    @Autowired private CompleteBookingAlertRequestProcessor completeBookingAlertRequestProcessor;
    @Autowired private CompleteConsumptionRequestProcessor completeConsumptionRequestProcessor;
    @Autowired private CompleteExternalBookingRequestProcessor completeExternalBookingRequestProcessor;

    @PostConstruct
    public void init() {
        super.put(CompleteAvailabilityRequestType.class, completeAvailabilityRequestProcessor);
        super.put(CompletePlaceAvailabilityRequestType.class, completePlaceAvailabilityRequestProcessor);
        super.put(CompleteBookingAlertRequestType.class, completeBookingAlertRequestProcessor);
        super.put(CompleteConsumptionRequestType.class, completeConsumptionRequestProcessor);
        super.put(CompleteExternalBookingRequestType.class, completeExternalBookingRequestProcessor);
        log.trace("Ready");
    }

    public SubscriptionRequestMessageProcessor find(RequestMessageGroup s) {
        Class<?> clazz = s.getClass();
        SubscriptionRequestMessageProcessor p = super.get(clazz);
        if (p == null) {
            throw new IxsiProcessingException("No processor is registered for the incoming request of type: " + clazz);
        } else {
            return p;
        }
    }
}