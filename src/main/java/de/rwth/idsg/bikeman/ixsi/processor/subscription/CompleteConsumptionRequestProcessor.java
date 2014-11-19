package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.schema.CompleteConsumptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteConsumptionResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2014
 */
@Component
public class CompleteConsumptionRequestProcessor implements
        SubscriptionRequestMessageProcessor<CompleteConsumptionRequestType, CompleteConsumptionResponseType> {

    @Override
    public CompleteConsumptionResponseType process(CompleteConsumptionRequestType request, String systemId) {
        return null;
    }

    @Override
    public CompleteConsumptionResponseType invalidSystem() {
        return null;
    }
}
