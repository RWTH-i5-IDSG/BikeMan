package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionStatusRequest;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionStatusResponse;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
public class AvailabilitySubscriptionStatusRequestProcessor implements
        Processor<AvailabilitySubscriptionStatusRequest, AvailabilitySubscriptionStatusResponse> {

    @Override
    public AvailabilitySubscriptionStatusResponse process(AvailabilitySubscriptionStatusRequest request) {
        return null;
    }
}