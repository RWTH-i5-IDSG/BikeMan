package de.rwth.idsg.bikeman.ixsi.api;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;

import javax.xml.bind.JAXBException;

/**
 * Created by max on 08/09/14.
 */
public interface Parser {
    void unmarshalIncoming(CommunicationContext context) throws JAXBException;
    void marshalOutgoing(CommunicationContext context) throws JAXBException;
}