package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.impl.BookingAlertStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.BookingAlertSubscriptionRequestType;
import xjc.schema.ixsi.BookingAlertSubscriptionResponseType;
import xjc.schema.ixsi.ErrorType;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingAlertSubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<BookingAlertSubscriptionRequestType, BookingAlertSubscriptionResponseType> {

    @Autowired private BookingAlertStore bookingAlertStore;

    @Override
    public BookingAlertSubscriptionResponseType process(BookingAlertSubscriptionRequestType request, String systemId) {

        List<String> bookingIds = request.getBookingID();

        if (request.isSetUnsubscription() && request.isUnsubscription()) {
            bookingAlertStore.unsubscribe(systemId, bookingIds);
        } else {
            bookingAlertStore.subscribe(systemId, bookingIds);
        }

        return new BookingAlertSubscriptionResponseType();
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingAlertSubscriptionResponseType buildError(ErrorType e) {
        return new BookingAlertSubscriptionResponseType().withError(e);
    }
}
