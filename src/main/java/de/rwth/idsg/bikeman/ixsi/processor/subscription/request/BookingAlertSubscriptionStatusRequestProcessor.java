package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.store.BookingAlertStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.BookingAlertSubscriptionStatusRequestType;
import xjc.schema.ixsi.BookingAlertSubscriptionStatusResponseType;
import xjc.schema.ixsi.ErrorType;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingAlertSubscriptionStatusRequestProcessor implements
        SubscriptionRequestProcessor<BookingAlertSubscriptionStatusRequestType, BookingAlertSubscriptionStatusResponseType> {

    @Autowired private BookingAlertStore bookingAlertStore;

    @Override
    public Class<BookingAlertSubscriptionStatusRequestType> getProcessingClass() {
        return BookingAlertSubscriptionStatusRequestType.class;
    }

    @Override
    public BookingAlertSubscriptionStatusResponseType process(BookingAlertSubscriptionStatusRequestType request, String systemId) {
        List<String> bookingIds = bookingAlertStore.getSubscriptions(systemId);

        return new BookingAlertSubscriptionStatusResponseType().withBookingID(bookingIds);
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingAlertSubscriptionStatusResponseType buildError(ErrorType e) {
        return new BookingAlertSubscriptionStatusResponseType().withError(e);
    }

}
