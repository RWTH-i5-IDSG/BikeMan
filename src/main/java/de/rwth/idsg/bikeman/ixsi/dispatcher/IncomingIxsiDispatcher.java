package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.IxsiMessageType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.09.2014
 */
@Slf4j
@Component
public class IncomingIxsiDispatcher implements Dispatcher {

    @Autowired private Producer producer;
    @Autowired private QueryRequestTypeDispatcher queryRequestTypeDispatcher;
    @Autowired private SubscriptionRequestTypeDispatcher subscriptionRequestTypeDispatcher;

    @Override
    public void handle(CommunicationContext context) {
        log.trace("Entered handle...");

        IxsiMessageType incoming = context.getIncomingIxsi();

        if (incoming.isSetRequest()) {
            queryRequestTypeDispatcher.handle(context);

        } else if (incoming.isSetSubscriptionRequest()) {
            subscriptionRequestTypeDispatcher.handle(context);

        } else {
            throw new IxsiProcessingException("Incoming message must be a QueryRequest or SubscriptionRequest");
        }

        producer.send(context);
    }
}
