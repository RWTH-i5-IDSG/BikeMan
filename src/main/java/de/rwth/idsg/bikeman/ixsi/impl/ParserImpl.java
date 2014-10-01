package de.rwth.idsg.bikeman.ixsi.impl;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.api.Parser;
import de.rwth.idsg.bikeman.ixsi.schema.IxsiMessageType;
import de.rwth.idsg.bikeman.ixsi.schema.ObjectFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
@Component
public class ParserImpl implements Parser {

    @Autowired private JAXBContext jaxbContext;
    private static final ObjectFactory objectFactory = new ObjectFactory();

    @Override
    public void unmarshalIncoming(CommunicationContext context) throws JAXBException {
        log.trace("Entered unmarshalIncoming...");

        Unmarshaller um = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(context.getIncomingString());
        StreamSource source = new StreamSource(reader);
        JAXBElement<IxsiMessageType> incoming = um.unmarshal(source, IxsiMessageType.class);
        context.setIncomingIxsi(incoming.getValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void marshalOutgoing(CommunicationContext context) throws JAXBException {
        log.trace("Entered marshalOutgoing...");

        JAXBElement<IxsiMessageType> outgoing = objectFactory.createIxsi(context.getOutgoingIxsi());
        Marshaller m = jaxbContext.createMarshaller();
        // Pretty print?
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        // Drop the XML declaration?
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter stringWriter = new StringWriter();
        m.marshal(outgoing, stringWriter);
        context.setOutgoingString(stringWriter.toString());
    }
}