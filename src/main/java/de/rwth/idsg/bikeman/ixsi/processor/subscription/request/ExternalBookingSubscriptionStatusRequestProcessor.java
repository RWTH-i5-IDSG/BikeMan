package de.rwth.idsg.bikeman.ixsi.processor.subscription.request;

import de.rwth.idsg.bikeman.ixsi.impl.ExternalBookingStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.ExternalBookingSubscriptionStatusRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ExternalBookingSubscriptionStatusResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.02.2015
 */
@Component
public class ExternalBookingSubscriptionStatusRequestProcessor implements
        SubscriptionRequestProcessor<ExternalBookingSubscriptionStatusRequestType, ExternalBookingSubscriptionStatusResponseType> {

    @Autowired
    private ExternalBookingStore externalBookingStore;

    @Override
    public ExternalBookingSubscriptionStatusResponseType process(ExternalBookingSubscriptionStatusRequestType request, String systemId) {
        List<UserInfoType> subscribedUsers = externalBookingStore.getSubscriptions(systemId);

        return new ExternalBookingSubscriptionStatusResponseType().withUserInfo(subscribedUsers);
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public ExternalBookingSubscriptionStatusResponseType buildError(ErrorType e) {
        return new ExternalBookingSubscriptionStatusResponseType().withError(e);
    }
}
