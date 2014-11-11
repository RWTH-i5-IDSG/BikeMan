package de.rwth.idsg.bikeman.ixsi.impl;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.WebSocketSessionStore;
import de.rwth.idsg.bikeman.ixsi.api.Parser;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import de.rwth.idsg.bikeman.ixsi.schema.IxsiMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Set;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
@Component
public class ProducerImpl implements Producer {

    @Autowired private Parser parser;
    @Autowired private WebSocketSessionStore webSocketSessionStore;

    @Override
    public void send(CommunicationContext context) {
        // Convenience method. Just delegate
        sendInternal(context.getOutgoingIxsi(), context.getSession());
    }

    @Override
    public void send(IxsiMessageType ixsi, Set<String> systemIdSet) {
        for (String systemId : systemIdSet) {
            WebSocketSession session = webSocketSessionStore.getNext(systemId);
            sendInternal(ixsi, session);
        }
    }

    private void sendInternal(IxsiMessageType ixsi, WebSocketSession session) {
        log.trace("Entered sendInternal...");

        try {
            String str = parser.marshal(ixsi);
            TextMessage out = new TextMessage(str);
            session.sendMessage(out);

        } catch (JAXBException e) {
            throw new IxsiProcessingException("Could not marshal outgoing message", e);

        } catch (IOException e) {
            log.error("Exception happened", e);
        }
    }

}
