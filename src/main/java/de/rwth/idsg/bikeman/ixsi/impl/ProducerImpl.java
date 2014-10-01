package de.rwth.idsg.bikeman.ixsi.impl;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.api.Parser;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
@Component
public class ProducerImpl implements Producer {

    @Autowired private Parser parser;

    @Override
    public void send(CommunicationContext context) {
        log.trace("Entered send...");

        try {
            parser.marshalOutgoing(context);
            TextMessage out = new TextMessage(context.getOutgoingString());
            context.getSession().sendMessage(out);

        } catch (JAXBException | IOException e) {
            log.error("Exception happened", e);
            //TODO send error message
        }
    }
}