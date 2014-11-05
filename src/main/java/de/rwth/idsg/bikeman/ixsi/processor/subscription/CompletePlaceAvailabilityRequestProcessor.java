package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.CompletePlaceAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompletePlaceAvailabilityResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class CompletePlaceAvailabilityRequestProcessor implements
        SubscriptionRequestMessageProcessor<CompletePlaceAvailabilityRequestType, CompletePlaceAvailabilityResponseType> {

    @Override
    public CompletePlaceAvailabilityResponseType process(CompletePlaceAvailabilityRequestType request, String systemId) {
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public CompletePlaceAvailabilityResponseType invalidSystem() {
        CompletePlaceAvailabilityResponseType b = new CompletePlaceAvailabilityResponseType();
        b.getError().add(ErrorFactory.invalidSystem());
        return b;
    }
}
