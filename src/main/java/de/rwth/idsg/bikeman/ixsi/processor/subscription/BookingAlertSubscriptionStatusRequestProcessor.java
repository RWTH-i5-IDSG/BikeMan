package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingAlertSubscriptionStatusRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingAlertSubscriptionStatusResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingAlertSubscriptionStatusRequestProcessor implements
        SubscriptionRequestProcessor<BookingAlertSubscriptionStatusRequestType, BookingAlertSubscriptionStatusResponseType> {

    @Override
    public BookingAlertSubscriptionStatusResponseType process(BookingAlertSubscriptionStatusRequestType request) {
        // TODO FUTURE
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingAlertSubscriptionStatusResponseType invalidSystem() {
        BookingAlertSubscriptionStatusResponseType b = new BookingAlertSubscriptionStatusResponseType();
        b.getError().add(ErrorFactory.invalidSystem());
        return b;
    }
}