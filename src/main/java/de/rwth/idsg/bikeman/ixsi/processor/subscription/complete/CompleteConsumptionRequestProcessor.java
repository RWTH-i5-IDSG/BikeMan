package de.rwth.idsg.bikeman.ixsi.processor.subscription.complete;

import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestMessageProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteConsumptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteConsumptionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
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
    public CompleteConsumptionResponseType buildError(ErrorType e) {
        CompleteConsumptionResponseType b = new CompleteConsumptionResponseType();
        b.getError().add(e);
        return b;
    }
}
