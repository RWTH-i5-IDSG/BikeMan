package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.BookingAlertSubscriptionStatusRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingAlertSubscriptionStatusResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import org.springframework.stereotype.Component;

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
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingAlertSubscriptionStatusResponseType buildError(ErrorType e) {
        return new BookingAlertSubscriptionStatusResponseType()
            .withError(e);
    }

}
