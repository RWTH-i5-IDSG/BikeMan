package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteAvailabilityResponseType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
public class CompleteAvailabilityRequestProcessor implements
        Processor<CompleteAvailabilityRequestType, CompleteAvailabilityResponseType> {

    @Override
    public CompleteAvailabilityResponseType process(CompleteAvailabilityRequestType request) {
        return null;
    }
}