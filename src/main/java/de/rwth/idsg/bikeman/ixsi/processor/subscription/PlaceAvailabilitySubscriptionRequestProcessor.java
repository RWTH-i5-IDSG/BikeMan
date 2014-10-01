package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionResponseType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
public class PlaceAvailabilitySubscriptionRequestProcessor implements
        Processor<PlaceAvailabilitySubscriptionRequestType, PlaceAvailabilitySubscriptionResponseType> {

    @Override
    public PlaceAvailabilitySubscriptionResponseType process(PlaceAvailabilitySubscriptionRequestType request) {
        return null;
    }
}