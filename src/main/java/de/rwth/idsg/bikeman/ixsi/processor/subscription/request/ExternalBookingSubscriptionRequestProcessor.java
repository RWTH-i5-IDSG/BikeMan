package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.impl.ExternalBookingStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.ExternalBookingSubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ExternalBookingSubscriptionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.02.2015
 */
@Component
public class ExternalBookingSubscriptionRequestProcessor implements
        SubscriptionRequestProcessor<ExternalBookingSubscriptionRequestType, ExternalBookingSubscriptionResponseType> {

    @Autowired private ExternalBookingStore externalBookingStore;

    @Override
    public ExternalBookingSubscriptionResponseType process(ExternalBookingSubscriptionRequestType request, String systemId) {

        List<UserInfoType> users = request.getUserInfo();

        if (request.isSetUnsubscription() && request.isUnsubscription()) {
            externalBookingStore.unsubscribe(systemId, users);
        } else {
            externalBookingStore.subscribe(systemId, users);
        }

        return new ExternalBookingSubscriptionResponseType();
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public ExternalBookingSubscriptionResponseType buildError(ErrorType e) {
        return new ExternalBookingSubscriptionResponseType().withError(e);
    }
}
