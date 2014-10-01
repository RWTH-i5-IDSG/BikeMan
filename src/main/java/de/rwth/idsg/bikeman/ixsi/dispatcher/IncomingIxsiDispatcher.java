package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.schema.QueryRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.SubscriptionRequestType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeConfigurationException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.09.2014
 */
@Slf4j
@Component
public class IncomingIxsiDispatcher implements Dispatcher {

    @Autowired private QueryRequestTypeDispatcher queryRequestTypeDispatcher;
    @Autowired private SubscriptionRequestTypeDispatcher subscriptionRequestTypeDispatcher;

    private Map<Class, Dispatcher> map;

    @PostConstruct
    public void init() throws DatatypeConfigurationException {
        map = new HashMap<>();
        map.put(QueryRequestType.class, queryRequestTypeDispatcher);
        map.put(SubscriptionRequestType.class, subscriptionRequestTypeDispatcher);
    }

    @Override
    public void handle(CommunicationContext context) {
        log.trace("Entered handle...");

        /**
         * Explanation of why getting only the first element in list with "get(0)":
         *
         * The incoming message list does not have to contain only one element.
         * IXSI.xsd defines that the root IXSI element can contain either
         *
         * MULTIPLE Requests (<xs:element name="Request" type="QueryRequestType" maxOccurs="unbounded">)
         * OR
         * ONE SubscriptionRequest (<xs:element name="SubscriptionRequest" type="SubscriptionRequestType">),
         *
         * since these are included within a <xs:choice> environment.
         *
         * --
         *
         * This means, that the request type is ALWAYS of the same kind (No mix of QueryRequestType with SubscriptionRequestType).
         * And because we only want to route the incoming IXSI based on the request data type,
         * it's good enough to look at the first element. Proper handling of multiple QueryRequestTypes
         * is done in {@link de.rwth.idsg.bikeman.ixsi.dispatcher.QueryRequestTypeDispatcher}.
         */
        Object message = context.getIncomingIxsi().getMessageList().get(0);

        Class<?> clazz = message.getClass();
        Dispatcher d = map.get(clazz);
        if (d == null) {
            throw new IllegalArgumentException("No dispatcher is registered for the incoming message: " + context.getIncomingString());
        } else {
            log.trace("Handling class: {}", clazz);
            d.handle(context);
        }
    }
}