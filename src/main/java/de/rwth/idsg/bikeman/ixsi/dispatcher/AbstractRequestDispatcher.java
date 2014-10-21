package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.schema.SystemIDType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.09.2014
 */
@Slf4j
public abstract class AbstractRequestDispatcher implements Dispatcher {

    @Override
    public void handle(CommunicationContext context) {
        // Override in extending classes
    }

    public boolean validateSender(SystemIDType systemIDType) {
        //TODO
        return true;
    }
}