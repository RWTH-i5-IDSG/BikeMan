package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.schema.HeartBeatRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.HeartBeatResponseType;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class HeartBeatRequestProcessor implements
        Processor<HeartBeatRequestType, HeartBeatResponseType> {

    @Override
    public HeartBeatResponseType process(HeartBeatRequestType request) {
        return null;
    }
}