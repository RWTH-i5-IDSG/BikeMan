package de.rwth.idsg.bikeman.ixsi.endpoint;

import de.rwth.idsg.bikeman.config.IxsiConfiguration;
import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.store.WebSocketSessionStore;
import de.rwth.idsg.bikeman.ixsi.store.AvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.store.BookingAlertStore;
import de.rwth.idsg.bikeman.ixsi.store.ConsumptionStore;
import de.rwth.idsg.bikeman.ixsi.store.ExternalBookingStore;
import de.rwth.idsg.bikeman.ixsi.store.PlaceAvailabilityStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Deque;
import java.util.Map;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
@Component
public class WebSocketEndpoint extends ConcurrentTextWebSocketHandler {

    @Autowired private WebSocketSessionStore webSocketSessionStore;
    @Autowired private Consumer consumer;

    @Autowired private AvailabilityStore availabilityStore;
    @Autowired private ConsumptionStore consumptionStore;
    @Autowired private ExternalBookingStore externalBookingStore;
    @Autowired private PlaceAvailabilityStore placeAvailabilityStore;
    @Autowired private BookingAlertStore bookingAlertStore;

    /**
     * Close the sessions for a graceful shutdown
     */
    @PreDestroy
    public void destroy() {
        Map<String, Deque<WebSocketSession>> sessionMap = webSocketSessionStore.getLookupTable();

        for (Deque<WebSocketSession> sessionsForOneSystem : sessionMap.values()) {
            for (WebSocketSession session : sessionsForOneSystem) {
                closeSession(session);
            }
        }
    }

    @Override
    public void onMessage(WebSocketSession session, TextMessage webSocketMessage) throws Exception {
        log.info("[id={}] Received message: {}", session.getId(), webSocketMessage.getPayload());

        String payload = webSocketMessage.getPayload();
        CommunicationContext context = new CommunicationContext(session, payload);
        try {
            consumer.consume(context);
        } catch (IxsiProcessingException e) {
            handleError(session, payload, e);
        }
    }

    @Override
    public void onOpen(WebSocketSession session) throws Exception {
        log.info("New connection established: {}", session);
        String systemId = (String) session.getAttributes().get(IxsiConfiguration.SYSTEM_ID_KEY);
        webSocketSessionStore.add(systemId, session);
    }

    @Override
    public void onClose(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("[id={}] Connection was closed, status: {}", session.getId(), closeStatus);
        String systemId = (String) session.getAttributes().get(IxsiConfiguration.SYSTEM_ID_KEY);

        int sizeAfter = webSocketSessionStore.remove(systemId, session);
        if (sizeAfter == 0) {
            unSubscribeStores(systemId);
        }
    }

    @Override
    public void onError(WebSocketSession session, Throwable throwable) throws Exception {
        log.error("Oops", throwable);
        // TODO catch transportexceptions!
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

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

    private void closeSession(WebSocketSession session) {
        if (session.isOpen()) {
            try {
                session.close(new CloseStatus(1001, "BikeMan is shutting down"));
            } catch (IOException e) {
                log.error("Failed to close the session", e);
            }
        }
    }

    private void unSubscribeStores(String systemId) {
        log.debug("There are no open connections left to system '{}'. "
                + "Removing it from all the subscription stores", systemId);

        availabilityStore.unsubscribeAll(systemId);
        consumptionStore.unsubscribeAll(systemId);
        externalBookingStore.unsubscribeAll(systemId);
        placeAvailabilityStore.unsubscribeAll(systemId);
        bookingAlertStore.unsubscribeAll(systemId);
    }
}
