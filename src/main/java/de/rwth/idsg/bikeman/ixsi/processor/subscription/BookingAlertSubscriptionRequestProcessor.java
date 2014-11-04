package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilitySubscriptionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingAlertSubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingAlertSubscriptionResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class BookingAlertSubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<BookingAlertSubscriptionRequestType, BookingAlertSubscriptionResponseType> {

    @Override
    public BookingAlertSubscriptionResponseType process(BookingAlertSubscriptionRequestType request) {
        // TODO FUTURE
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingAlertSubscriptionResponseType invalidSystem() {
        BookingAlertSubscriptionResponseType b = new BookingAlertSubscriptionResponseType();
        b.getError().add(ErrorFactory.invalidSystem());
        return b;
    }
}