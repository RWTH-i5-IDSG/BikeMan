package de.rwth.idsg.bikeman.ixsi.impl;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.api.Consumer;
import de.rwth.idsg.bikeman.ixsi.api.Parser;
import de.rwth.idsg.bikeman.ixsi.dispatcher.IncomingIxsiDispatcher;
import de.rwth.idsg.bikeman.ixsi.schema.IxsiMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;

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
            IxsiMessageType i = parser.unmarshal(context.getIncomingString());
            context.setIncomingIxsi(i);
            dispatcher.handle(context);

        } catch (JAXBException e) {
            throw new IxsiProcessingException("Could not unmarshal incoming message", e);
        }
    }
}
