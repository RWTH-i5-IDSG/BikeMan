package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.BookingAlertSubscriptionStatusRequestType;
import xjc.schema.ixsi.BookingAlertSubscriptionStatusResponseType;
import xjc.schema.ixsi.ErrorType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingAlertSubscriptionStatusRequestProcessor implements
        SubscriptionRequestProcessor<BookingAlertSubscriptionStatusRequestType, BookingAlertSubscriptionStatusResponseType> {

    @Override
    public BookingAlertSubscriptionStatusResponseType process(BookingAlertSubscriptionStatusRequestType request, String systemId) {
        // TODO FUTURE
        return buildError(ErrorFactory.Sys.notImplemented(null, null));
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingAlertSubscriptionStatusResponseType buildError(ErrorType e) {
        return new BookingAlertSubscriptionStatusResponseType().withError(e);
    }

}
