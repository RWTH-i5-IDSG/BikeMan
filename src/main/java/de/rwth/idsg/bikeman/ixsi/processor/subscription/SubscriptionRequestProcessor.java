package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.schema.SubscriptionRequestGroup;
import de.rwth.idsg.bikeman.ixsi.schema.SubscriptionResponseGroup;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 21.10.2014
 */
public interface SubscriptionRequestProcessor<T1 extends SubscriptionRequestGroup, T2 extends SubscriptionResponseGroup> {
    T2 process(T1 request);
}