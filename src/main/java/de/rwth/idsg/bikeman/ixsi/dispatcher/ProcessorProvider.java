package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.processor.api.StaticRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestMessageProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.ixsi.jaxb.RequestMessageGroup;
import de.rwth.idsg.ixsi.jaxb.StaticDataRequestGroup;
import de.rwth.idsg.ixsi.jaxb.SubscriptionRequestGroup;
import de.rwth.idsg.ixsi.jaxb.UserTriggeredRequestChoice;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.02.2016
 */
public interface ProcessorProvider {

    StaticRequestProcessor find(StaticDataRequestGroup s);

    UserRequestProcessor find(UserTriggeredRequestChoice s);

    SubscriptionRequestProcessor find(SubscriptionRequestGroup s);

    SubscriptionRequestMessageProcessor find(RequestMessageGroup s);
}
