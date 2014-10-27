package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteAvailabilityResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class CompleteAvailabilityRequestProcessor implements
        SubscriptionRequestMessageProcessor<CompleteAvailabilityRequestType, CompleteAvailabilityResponseType> {

    @Override
    public CompleteAvailabilityResponseType process(CompleteAvailabilityRequestType request) {
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public CompleteAvailabilityResponseType invalidSystem() {
        CompleteAvailabilityResponseType b = new CompleteAvailabilityResponseType();
        b.getError().add(ErrorFactory.invalidSystem());
        return b;
    }
}