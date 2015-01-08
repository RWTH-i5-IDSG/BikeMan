package de.rwth.idsg.bikeman.ixsi.api;

import org.springframework.web.socket.WebSocketSession;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 06.11.2014
 */
public interface WebSocketSessionStore {
    void add(String systemID, WebSocketSession session);
    void remove(String systemID, WebSocketSession session);
    WebSocketSession getNext(String systemID);
    void clear();
}
