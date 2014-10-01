package de.rwth.idsg.bikeman.ixsi.impl;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.api.Consumer;
import de.rwth.idsg.bikeman.ixsi.api.Parser;
import de.rwth.idsg.bikeman.ixsi.dispatcher.IncomingIxsiDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        try {
            parser.unmarshalIncoming(context);
            dispatcher.handle(context);

        } catch (Exception e) {
            log.error("Exception happened", e);
            //TODO send error message
        }
    }
}