package de.rwth.idsg.bikeman.ixsi.api;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;

/**
 * Created by max on 08/09/14.
 */
public interface Consumer {
    void consume(CommunicationContext context);
}