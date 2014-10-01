package de.rwth.idsg.bikeman.ixsi;

import de.rwth.idsg.bikeman.ixsi.api.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
@Component
public class WebSocketEndpoint extends TextWebSocketHandler {

    @Autowired private Consumer consumer;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage webSocketMessage) throws Exception {
        log.info("[id={}] Received text message: {}", session.getId(), webSocketMessage);

        CommunicationContext context = new CommunicationContext(session, webSocketMessage.getPayload());
        consumer.consume(context);
        //this.sendMessage(webSocketMessage.getPayload() + " -- server");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("New connection established: {}", session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("[id={}] Connection was closed, status: {}", session.getId(), closeStatus);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        // TODO catch transportexceptions!
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}
