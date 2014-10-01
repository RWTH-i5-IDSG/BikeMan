package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionResponseType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
public class AvailabilitySubscriptionRequestProcessor implements
        Processor<AvailabilitySubscriptionRequestType, AvailabilitySubscriptionResponseType> {

    @Override
    public AvailabilitySubscriptionResponseType process(AvailabilitySubscriptionRequestType request) {
        return null;
    }
}