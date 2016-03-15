package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.store.ExternalBookingStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.ErrorType;
import xjc.schema.ixsi.ExternalBookingSubscriptionRequestType;
import xjc.schema.ixsi.ExternalBookingSubscriptionResponseType;
import xjc.schema.ixsi.UserInfoType;

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
    public Class<ExternalBookingSubscriptionRequestType> getProcessingClass() {
        return ExternalBookingSubscriptionRequestType.class;
    }

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
