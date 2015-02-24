package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.ExternalBookingSubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ExternalBookingSubscriptionResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.02.2015
 */
@Component
public class ExternalBookingSubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<ExternalBookingSubscriptionRequestType, ExternalBookingSubscriptionResponseType> {

    @Override
    public ExternalBookingSubscriptionResponseType process(ExternalBookingSubscriptionRequestType request, String systemId) {
        // TODO
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public ExternalBookingSubscriptionResponseType buildError(ErrorType e) {
        return new ExternalBookingSubscriptionResponseType().withError(e);
    }
}
