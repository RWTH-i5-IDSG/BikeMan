package de.rwth.idsg.bikeman.ixsi.endpoint;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.endpoint.Consumer;
import de.rwth.idsg.bikeman.ixsi.endpoint.Parser;
import de.rwth.idsg.bikeman.ixsi.dispatcher.IncomingIxsiDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.IxsiMessageType;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
@Component
public class ConsumerImpl implements Consumer {

    @Autowired private Parser parser;
    @Autowired private IncomingIxsiDispatcher dispatcher;

    @Override
    public void consume(CommunicationContext context) {
        log.trace("Entered consume...");

        IxsiMessageType i = parser.unmarshal(context.getIncomingString());
        context.setIncomingIxsi(i);
        dispatcher.handle(context);
    }
}
