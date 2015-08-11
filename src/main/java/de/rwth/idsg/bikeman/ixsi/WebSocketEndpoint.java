package de.rwth.idsg.bikeman.ixsi;

import de.rwth.idsg.bikeman.ixsi.api.Consumer;
import de.rwth.idsg.bikeman.ixsi.api.WebSocketSessionStore;
import de.rwth.idsg.bikeman.ixsi.impl.AvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.impl.ConsumptionStore;
import de.rwth.idsg.bikeman.ixsi.impl.ExternalBookingStore;
import de.rwth.idsg.bikeman.ixsi.impl.PlaceAvailabilityStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
@Component
public class WebSocketEndpoint extends TextWebSocketHandler {

    private static final Object LOCK = new Object();

    @Autowired private WebSocketSessionStore webSocketSessionStore;
    @Autowired private Consumer consumer;

    @Autowired private AvailabilityStore availabilityStore;
    @Autowired private ConsumptionStore consumptionStore;
    @Autowired private ExternalBookingStore externalBookingStore;
    @Autowired private PlaceAvailabilityStore placeAvailabilityStore;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage webSocketMessage) throws Exception {
        log.info("[id={}] Received message: {}", session.getId(), webSocketMessage.getPayload());

        String payload = webSocketMessage.getPayload();
        CommunicationContext context = new CommunicationContext(session, payload);
        try {
            consumer.consume(context);
        } catch (IxsiProcessingException e) {
            handleError(session, payload, e);
        }
    }

    /**
     * If there's something fundamentally wrong with the incoming message or its processing
     * (like receiving non-Ixsi strings or parsing the request) from which the system cannot recover
     * and send the appropriate IXSI error message, we cannot do anything but send a simple error string
     * for debugging purposes and close the session.
     *
     */
    private void handleError(WebSocketSession session, String payload, IxsiProcessingException e) throws IOException {
        log.error("Error occurred", e);
        String errorMsg = "IxsiProcessingException: " + e.getLocalizedMessage();

        session.sendMessage(new TextMessage(errorMsg + "\nMessage that caused the exception: " + payload));
        session.close(CloseStatus.NOT_ACCEPTABLE.withReason(errorMsg));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("New connection established: {}", session);
        String systemId = (String) session.getAttributes().get(HandshakeInterceptor.SYSTEM_ID_KEY);
        webSocketSessionStore.add(systemId, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("[id={}] Connection was closed, status: {}", session.getId(), closeStatus);
        String systemId = (String) session.getAttributes().get(HandshakeInterceptor.SYSTEM_ID_KEY);
        synchronized (LOCK) {
            webSocketSessionStore.remove(systemId, session);

            if (webSocketSessionStore.size(systemId) == 0) {
                log.debug("There are no open connections left to system '{}'. "
                    + "Removing it from all the subscription stores", systemId);

                availabilityStore.unsubscribeAll(systemId);
                consumptionStore.unsubscribeAll(systemId);
                externalBookingStore.unsubscribeAll(systemId);
                placeAvailabilityStore.unsubscribeAll(systemId);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        log.error("Oops", throwable);
        // TODO catch transportexceptions!
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
