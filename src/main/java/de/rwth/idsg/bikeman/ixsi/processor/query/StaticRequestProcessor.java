package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.schema.StaticDataRequestGroup;
import de.rwth.idsg.bikeman.ixsi.schema.StaticDataResponseGroup;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 21.10.2014
 */
public interface StaticRequestProcessor<T1 extends StaticDataRequestGroup, T2 extends StaticDataResponseGroup> {
    T2 process(T1 request);
}