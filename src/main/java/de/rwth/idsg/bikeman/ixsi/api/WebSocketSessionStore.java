package de.rwth.idsg.bikeman.ixsi.api;

import com.google.common.base.Optional;
import org.springframework.web.socket.WebSocketSession;

import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 06.11.2014
 */
public interface WebSocketSessionStore {
    void add(String systemID, WebSocketSession session);
    void remove(String systemID, WebSocketSession session);
    Optional<WebSocketSession> get(String systemID, String sessionID);
    WebSocketSession getNext(String systemID);
    int size(String systemID);
    void clear();

    ConcurrentHashMap<String, Deque<WebSocketSession>> getLookupTable();
}
