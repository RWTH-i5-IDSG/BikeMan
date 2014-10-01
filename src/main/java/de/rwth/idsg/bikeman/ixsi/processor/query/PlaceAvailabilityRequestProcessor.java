package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityResponseType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
public class PlaceAvailabilityRequestProcessor implements
        Processor<PlaceAvailabilityRequestType, PlaceAvailabilityResponseType> {

    @Override
    public PlaceAvailabilityResponseType process(PlaceAvailabilityRequestType request) {
        return null;
    }
}