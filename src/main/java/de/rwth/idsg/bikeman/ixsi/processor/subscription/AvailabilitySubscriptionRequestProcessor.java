package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class AvailabilitySubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<AvailabilitySubscriptionRequestType, AvailabilitySubscriptionResponseType> {

    @Override
    public AvailabilitySubscriptionResponseType process(AvailabilitySubscriptionRequestType request) {
        return null;
    }
}