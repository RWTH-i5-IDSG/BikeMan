package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionSubscriptionStatusRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionSubscriptionStatusResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2014
 */
@Component
public class ConsumptionSubscriptionStatusRequestProcessor implements
        SubscriptionRequestProcessor<ConsumptionSubscriptionStatusRequestType, ConsumptionSubscriptionStatusResponseType> {

    @Override
    public ConsumptionSubscriptionStatusResponseType process(ConsumptionSubscriptionStatusRequestType request, String systemId) {
        return null;
    }

    @Override
    public ConsumptionSubscriptionStatusResponseType invalidSystem() {
        return null;
    }
}
