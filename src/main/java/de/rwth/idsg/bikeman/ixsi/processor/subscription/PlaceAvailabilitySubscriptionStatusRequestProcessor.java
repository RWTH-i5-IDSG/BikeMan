package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionStatusRequest;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilitySubscriptionStatusResponse;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
public class PlaceAvailabilitySubscriptionStatusRequestProcessor implements
        Processor<PlaceAvailabilitySubscriptionStatusRequest, PlaceAvailabilitySubscriptionStatusResponse> {

    @Override
    public PlaceAvailabilitySubscriptionStatusResponse process(PlaceAvailabilitySubscriptionStatusRequest request) {
        return null;
    }
}