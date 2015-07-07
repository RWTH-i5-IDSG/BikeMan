package de.rwth.idsg.bikeman.ixsi.processor.subscription.complete;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestMessageProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteExternalBookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteExternalBookingResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.02.2015
 */
@Component
public class CompleteExternalBookingRequestProcessor implements
        SubscriptionRequestMessageProcessor<CompleteExternalBookingRequestType, CompleteExternalBookingResponseType> {

    @Override
    public CompleteExternalBookingResponseType process(CompleteExternalBookingRequestType request, String systemId) {
        // TODO FUTURE
        return buildError(ErrorFactory.Sys.notImplemented(null, null));
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public CompleteExternalBookingResponseType buildError(ErrorType e) {
        return new CompleteExternalBookingResponseType()
            .withError(e)
            .withMessageBlockID("none");
    }
}
