package de.rwth.idsg.bikeman.ixsi.impl;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.api.Parser;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import de.rwth.idsg.bikeman.ixsi.api.WebSocketSessionStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import xjc.schema.ixsi.IxsiMessageType;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Set;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
@Component
public class ProducerImpl implements Producer {

    private static final Object LOCK = new Object();

    @Autowired private Parser parser;
    @Autowired private WebSocketSessionStore webSocketSessionStore;

    @Override
    public void send(CommunicationContext context) {
        try {
            String str = parser.marshal(context.getOutgoingIxsi());
            TextMessage out = new TextMessage(str);

            WebSocketSession session = context.getSession();
            log.debug("[id: {}] Sending message: {}", session.getId(), str);
            session.sendMessage(out);

        } catch (JAXBException e) {
            throw new IxsiProcessingException("Could not marshal outgoing message", e);

        } catch (Exception e) {
            log.error("Exception happened", e);
        }
    }

    @Override
    public void send(IxsiMessageType ixsi, Set<String> systemIdSet) {
        try {
            String str = parser.marshal(ixsi);
            TextMessage out = new TextMessage(str);

            for (String systemId : systemIdSet) {
                push(systemId, out);
            }
        } catch (JAXBException e) {
            throw new IxsiProcessingException("Could not marshal outgoing message", e);
        }
    }

    /**
     * Push message exceptions should be handled silently, that is they should not take the ongoing process/thread or
     * system down. With this, we just log the exception and allow the application to continue with the next system in
     * line when the current push fails.
     */
    private void push(String systemId, TextMessage out) {
        try {
            WebSocketSession session = webSocketSessionStore.getNext(systemId);
            log.debug("[id: {}] Sending message: {}", session.getId(), out.getPayload());
            session.sendMessage(out);

        } catch (Exception e) {
            log.error("Exception happened", e);
        }
    }

//    /**
//     * Dirty, dirty hack using synchronized to prevent exceptions like:
//     *
//     * IllegalStateException:
//     * The remote endpoint was in state [TEXT_PARTIAL_WRITING] which is an invalid state for called method
//     *
//     * This happens, when req/res communication and sub push messages try to use the same session at the same time.
//     */
//    private void synchronizedSend(WebSocketSession session, TextMessage out) throws IOException {
//        synchronized (LOCK) {
//            session.sendMessage(out);
//        }
//    }
}
