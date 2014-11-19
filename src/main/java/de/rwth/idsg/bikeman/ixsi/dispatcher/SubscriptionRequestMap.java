package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.AvailabilitySubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.AvailabilitySubscriptionStatusRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.BookingAlertSubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.BookingAlertSubscriptionStatusRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.ConsumptionSubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.ConsumptionSubscriptionStatusRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.PlaceAvailabilitySubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.PlaceAvailabilitySubscriptionStatusRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionStatusRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingAlertSubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingAlertSubscriptionStatusRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionSubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionSubscriptionStatusRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionStatusRequestType;
import de.rwth.idsg.ixsi.jaxb.SubscriptionRequestGroup;
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
public class SubscriptionRequestMap extends HashMap<Class<?>, SubscriptionRequestProcessor> {
    private static final long serialVersionUID = -8483982822596525355L;

    @Autowired private AvailabilitySubscriptionRequestProcessor availabilitySubscriptionRequestProcessor;
    @Autowired private AvailabilitySubscriptionStatusRequestProcessor availabilitySubscriptionStatusRequestProcessor;
    @Autowired private PlaceAvailabilitySubscriptionRequestProcessor placeAvailabilitySubscriptionRequestProcessor;
    @Autowired private PlaceAvailabilitySubscriptionStatusRequestProcessor placeAvailabilitySubscriptionStatusRequestProcessor;
    @Autowired private BookingAlertSubscriptionRequestProcessor bookingAlertSubscriptionRequestProcessor;
    @Autowired private BookingAlertSubscriptionStatusRequestProcessor bookingAlertSubscriptionStatusRequestProcessor;
    @Autowired private ConsumptionSubscriptionRequestProcessor consumptionSubscriptionRequestProcessor;
    @Autowired private ConsumptionSubscriptionStatusRequestProcessor consumptionSubscriptionStatusRequestProcessor;

    public SubscriptionRequestMap() {
        super();
        log.trace("Initialized");
    }

    @PostConstruct
    public void init() {
        super.put(AvailabilitySubscriptionRequestType.class, availabilitySubscriptionRequestProcessor);
        super.put(AvailabilitySubscriptionStatusRequestType.class, availabilitySubscriptionStatusRequestProcessor);
        super.put(PlaceAvailabilitySubscriptionRequestType.class, placeAvailabilitySubscriptionRequestProcessor);
        super.put(PlaceAvailabilitySubscriptionStatusRequestType.class, placeAvailabilitySubscriptionStatusRequestProcessor);
        super.put(BookingAlertSubscriptionRequestType.class, bookingAlertSubscriptionRequestProcessor);
        super.put(BookingAlertSubscriptionStatusRequestType.class, bookingAlertSubscriptionStatusRequestProcessor);
        super.put(ConsumptionSubscriptionRequestType.class, consumptionSubscriptionRequestProcessor);
        super.put(ConsumptionSubscriptionStatusRequestType.class, consumptionSubscriptionStatusRequestProcessor);
        log.trace("Ready");
    }

    public SubscriptionRequestProcessor find(SubscriptionRequestGroup s) {
        Class<?> clazz = s.getClass();
        SubscriptionRequestProcessor p = super.get(clazz);
        if (p == null) {
            throw new IxsiProcessingException("No processor is registered for the incoming request of type: " + clazz);
        } else {
            return p;
        }
    }
}