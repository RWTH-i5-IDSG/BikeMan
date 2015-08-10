package de.rwth.idsg.bikeman.ixsi.impl;

import de.rwth.idsg.bikeman.ixsi.api.WebSocketSessionStore;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
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
    @Getter
    private final ConcurrentHashMap<String, Deque<WebSocketSession>> lookupTable = new ConcurrentHashMap<>();

    @Override
    public synchronized void add(String systemID, WebSocketSession session) {
        Deque<WebSocketSession> sessionSet = lookupTable.get(systemID);
        if (sessionSet == null) {
            sessionSet = new ArrayDeque<>();
            sessionSet.add(session);
            lookupTable.put(systemID, sessionSet);

        } else {
            sessionSet.addLast(session); // Adding at the end
        }
        log.debug("A new WebSocketSession with id '{}' is stored for system '{}' (size: {})",
            session.getId(), systemID, sessionSet.size());
    }

    @Override
    public synchronized void remove(String systemID, WebSocketSession session) {
        Deque<WebSocketSession> sessionSet = lookupTable.get(systemID);
        if (sessionSet == null) {
            return;
        }

        for (WebSocketSession wss : sessionSet) {
            if (wss.getId().equals(session.getId())) {
                if (sessionSet.remove(wss)) {
                    log.debug("The WebSocketSession with id '{}' is removed for system '{}' (size: {})",
                            session.getId(), systemID, sessionSet.size());
                } else {
                    log.error("Failed to remove the WebSocketSession with id '{}' for system '{}'",
                            session.getId(), systemID);
                }
            }
        }
    }

    /**
     * sessionSet.removeFirst() will throw NoSuchElementException, if the sessionSet is empty.
     *
     * But the sessionSet itself might be null, if there is no connection established and therefore,
     * the sessionSet is not initialized, yet. In this case we throw the same exception,
     * since the ProducerImpl decides on the context based on the exception type.
     */
    @Override
    public synchronized WebSocketSession getNext(String systemID) {
        Deque<WebSocketSession> sessionSet = lookupTable.get(systemID);
        if (sessionSet == null) {
            throw new NoSuchElementException();
        }
        // Get the first item, and add at the end
        WebSocketSession s = sessionSet.removeFirst();
        sessionSet.addLast(s);
        return s;
    }

    @Override
    public int size(String systemID) {
        Deque<WebSocketSession> sessionSet = lookupTable.get(systemID);
        return sessionSet.size();
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
