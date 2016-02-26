package de.rwth.idsg.bikeman.ixsi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;
import xjc.schema.ixsi.IxsiMessageType;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 23.09.2014
 */
@Getter
@Setter
public class CommunicationContext {

    private final WebSocketSession session;

    private final String incomingString;
    private String outgoingString;

    private IxsiMessageType incomingIxsi;
    private final IxsiMessageType outgoingIxsi;

    public CommunicationContext(WebSocketSession session, String incomingString) {
        this.session = session;
        this.incomingString = incomingString;

        // Early init outgoing type and access only via getter
        // during the processing chain to add responses
        this.outgoingIxsi = new IxsiMessageType();
    }
}
