package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.BookingAlertSubscriptionRequestType;
import xjc.schema.ixsi.BookingAlertSubscriptionResponseType;
import xjc.schema.ixsi.ErrorType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingAlertSubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<BookingAlertSubscriptionRequestType, BookingAlertSubscriptionResponseType> {

    @Override
    public BookingAlertSubscriptionResponseType process(BookingAlertSubscriptionRequestType request, String systemId) {
        // TODO FUTURE
        return buildError(ErrorFactory.Sys.notImplemented(null, null));
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingAlertSubscriptionResponseType buildError(ErrorType e) {
        return new BookingAlertSubscriptionResponseType().withError(e);
    }
}
