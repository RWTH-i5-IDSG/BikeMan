package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.schema.CompleteBookingAlertRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteBookingAlertResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class CompleteBookingAlertRequestProcessor implements
        SubscriptionRequestMessageProcessor<CompleteBookingAlertRequestType, CompleteBookingAlertResponseType> {

    @Override
    public CompleteBookingAlertResponseType process(CompleteBookingAlertRequestType request) {
        return null;
    }
}