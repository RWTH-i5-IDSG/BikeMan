package de.rwth.idsg.bikeman.ixsi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * We want to support multiple connections from a client system.
 *
 * 1) During a request/response communication, the response is sent using the same WebSocketSession,
 * since we reference it in a CommunicationContext and pass it on. At the end of the process chain
 * the Producer sends the response using the referenced WebSocketSession.
 *
 * 2) For the push messages we need a mechanism to select one WebSocketSession from the set.
 * This is done in a round robin fashion. See getNext().
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 05.11.2014
 */
@Slf4j
@Service
public class WebSocketSessionStoreImpl implements WebSocketSessionStore {

    /**
     * Key   (String)                  = ID of the client system
     * Value (Deque<WebSocketSession>) = WebSocket connections of the client system
     */
    private final ConcurrentHashMap<String, Deque<WebSocketSession>> lookupTable = new ConcurrentHashMap<>();

    @Override
    public void add(String systemID, WebSocketSession session) {
        Deque<WebSocketSession> sessionSet = lookupTable.get(systemID);
        if (sessionSet == null) {
            sessionSet = new ArrayDeque<>();
            sessionSet.add(session);
            lookupTable.put(systemID, sessionSet);

        } else {
            sessionSet.addLast(session); // Adding at the end
        }
        log.debug("A new WebSocketSession with id '{}' is stored for system '{}'", session.getId(), systemID);
    }

    @Override
    public void remove(String systemID, WebSocketSession session) {
        Deque<WebSocketSession> sessionSet = lookupTable.get(systemID);
        if (sessionSet != null) {
            sessionSet.remove(session);
            log.debug("The WebSocketSession with id '{}' is removed for system '{}'", session.getId(), systemID);
        }
    }

    @Override
    public WebSocketSession getNext(String systemID) {
        Deque<WebSocketSession> sessionSet = lookupTable.get(systemID);
        // Get the first item, and add at the end
        WebSocketSession s = sessionSet.removeFirst();
        sessionSet.addLast(s);
        return s;
    }

    @Override
    public void clear() {
        lookupTable.clear();
        log.debug("Cleared the WebSocketSession store");
    }

    @Override
    public String toString() {
        return lookupTable.toString();
    }
}