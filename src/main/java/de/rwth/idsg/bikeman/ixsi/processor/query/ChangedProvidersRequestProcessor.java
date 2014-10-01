package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.schema.ChangedProvidersRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangedProvidersResponseType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
public class ChangedProvidersRequestProcessor implements
        Processor<ChangedProvidersRequestType, ChangedProvidersResponseType> {

    @Override
    public ChangedProvidersResponseType process(ChangedProvidersRequestType request) {
        return null;
    }
}
