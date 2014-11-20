package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionSubscriptionStatusRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionSubscriptionStatusResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
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
    public ConsumptionSubscriptionStatusResponseType buildError(ErrorType e) {
        ConsumptionSubscriptionStatusResponseType b = new ConsumptionSubscriptionStatusResponseType();
        b.getError().add(e);
        return b;
    }
}
