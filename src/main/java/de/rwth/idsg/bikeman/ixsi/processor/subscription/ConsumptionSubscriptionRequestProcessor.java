package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionSubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionSubscriptionResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2014
 */
@Component
public class ConsumptionSubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<ConsumptionSubscriptionRequestType, ConsumptionSubscriptionResponseType> {

    @Override
    public ConsumptionSubscriptionResponseType process(ConsumptionSubscriptionRequestType request, String systemId) {
        return null;
    }

    @Override
    public ConsumptionSubscriptionResponseType invalidSystem() {
        return null;
    }
}
