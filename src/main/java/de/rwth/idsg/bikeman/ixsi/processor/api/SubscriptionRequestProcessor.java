package de.rwth.idsg.bikeman.ixsi.processor.api;

import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.ixsi.jaxb.SubscriptionRequestGroup;
import de.rwth.idsg.ixsi.jaxb.SubscriptionResponseGroup;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 21.10.2014
 */
public interface SubscriptionRequestProcessor<T1 extends SubscriptionRequestGroup, T2 extends SubscriptionResponseGroup> {
    T2 process(T1 request, String systemId);
    T2 buildError(ErrorType e);
}