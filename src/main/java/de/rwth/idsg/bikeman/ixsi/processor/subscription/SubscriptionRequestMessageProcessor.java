package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.schema.RequestMessageGroup;
import de.rwth.idsg.bikeman.ixsi.schema.ResponseMessageGroup;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 21.10.2014
 */
public interface SubscriptionRequestMessageProcessor<T1 extends RequestMessageGroup, T2 extends ResponseMessageGroup> {
    T2 process(T1 request);
    T2 invalidSystem();
}