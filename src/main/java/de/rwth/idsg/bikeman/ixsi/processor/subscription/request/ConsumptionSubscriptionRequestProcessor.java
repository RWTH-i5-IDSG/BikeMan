package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.impl.ConsumptionStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionSubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionSubscriptionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2014
 */
@Component
public class ConsumptionSubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<ConsumptionSubscriptionRequestType, ConsumptionSubscriptionResponseType> {

    @Autowired private ConsumptionStore consumptionStore;

    @Override
    public ConsumptionSubscriptionResponseType process(ConsumptionSubscriptionRequestType request, String systemId) {

        List<String> itemIds = new ArrayList<>();
        for (String id : request.getBookingID()) {
            itemIds.add(id);
        }

        if (request.isSetUnsubscription() && request.isUnsubscription()) {
            consumptionStore.unsubscribe(systemId, itemIds);
        } else {
            consumptionStore.subscribe(systemId, itemIds);
        }

        return new ConsumptionSubscriptionResponseType();
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public ConsumptionSubscriptionResponseType buildError(ErrorType e) {
        ConsumptionSubscriptionResponseType b = new ConsumptionSubscriptionResponseType();
        b.getError().add(e);
        return b;
    }

}
