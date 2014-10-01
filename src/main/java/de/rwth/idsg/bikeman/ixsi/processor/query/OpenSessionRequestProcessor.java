package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.schema.OpenSessionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.OpenSessionResponseType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
public class OpenSessionRequestProcessor implements
        Processor<OpenSessionRequestType, OpenSessionResponseType> {

    @Override
    public OpenSessionResponseType process(OpenSessionRequestType request) {
        return null;
    }
}