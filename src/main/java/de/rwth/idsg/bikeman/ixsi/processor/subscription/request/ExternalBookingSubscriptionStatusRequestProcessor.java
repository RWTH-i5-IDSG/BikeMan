package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.ExternalBookingSubscriptionStatusRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ExternalBookingSubscriptionStatusResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.02.2015
 */
@Component
public class ExternalBookingSubscriptionStatusRequestProcessor implements
        SubscriptionRequestProcessor<ExternalBookingSubscriptionStatusRequestType, ExternalBookingSubscriptionStatusResponseType> {

    @Override
    public ExternalBookingSubscriptionStatusResponseType process(ExternalBookingSubscriptionStatusRequestType request, String systemId) {
        // TODO
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public ExternalBookingSubscriptionStatusResponseType buildError(ErrorType e) {
        return new ExternalBookingSubscriptionStatusResponseType().withError(e);
    }
}
