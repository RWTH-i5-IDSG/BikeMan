package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class PlaceAvailabilitySubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<PlaceAvailabilitySubscriptionRequestType, PlaceAvailabilitySubscriptionResponseType> {

    @Override
    public PlaceAvailabilitySubscriptionResponseType process(PlaceAvailabilitySubscriptionRequestType request) {
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public PlaceAvailabilitySubscriptionResponseType invalidSystem() {
        PlaceAvailabilitySubscriptionResponseType b = new PlaceAvailabilitySubscriptionResponseType();
        b.getError().add(ErrorFactory.invalidSystem());
        return b;
    }
}