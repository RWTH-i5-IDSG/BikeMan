package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.store.ConsumptionStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.ConsumptionSubscriptionStatusRequestType;
import xjc.schema.ixsi.ConsumptionSubscriptionStatusResponseType;
import xjc.schema.ixsi.ErrorType;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2014
 */
@Component
public class ConsumptionSubscriptionStatusRequestProcessor implements
        SubscriptionRequestProcessor<ConsumptionSubscriptionStatusRequestType, ConsumptionSubscriptionStatusResponseType> {

    @Autowired private ConsumptionStore consumptionStore;

    @Override
    public Class<ConsumptionSubscriptionStatusRequestType> getProcessingClass() {
        return ConsumptionSubscriptionStatusRequestType.class;
    }

    @Override
    public ConsumptionSubscriptionStatusResponseType process(ConsumptionSubscriptionStatusRequestType request, String systemId) {
        List<String> bookingIds = consumptionStore.getSubscriptions(systemId);

        return new ConsumptionSubscriptionStatusResponseType().withBookingID(bookingIds);
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public ConsumptionSubscriptionStatusResponseType buildError(ErrorType e) {
        return new ConsumptionSubscriptionStatusResponseType().withError(e);
    }
}
