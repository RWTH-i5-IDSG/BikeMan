package de.rwth.idsg.bikeman.ixsi.endpoint;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.endpoint.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import xjc.schema.ixsi.IxsiMessageType;
import xjc.schema.ixsi.ObjectFactory;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

/**
 * Created by max on 08/09/14.
 */
@Slf4j
@Component
public class ParserImpl implements Parser {

    @Autowired private JAXBContext jaxbContext;

    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();
    private Schema schema;

    @PostConstruct
    public void init() throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL xsdURL = getClass().getClassLoader().getResource(IXSIConstants.XML_SCHEMA_FILE);
        if (xsdURL == null) {
            throw new IOException("XML schema could not be found/loaded");
        } else {
            schema = schemaFactory.newSchema(xsdURL);
        }
    }

    @Override
    public IxsiMessageType unmarshal(String str) {
        log.trace("Entered unmarshal...");

        try {
            Unmarshaller um = jaxbContext.createUnmarshaller();
            // Validate against the schema
            um.setSchema(schema);
            StringReader reader = new StringReader(str);
            StreamSource source = new StreamSource(reader);
            return um.unmarshal(source, IxsiMessageType.class).getValue();

        } catch (JAXBException e) {
            throw new IxsiProcessingException("Could not unmarshal incoming message: " + str, e);
        }
    }

    @Override
    public String marshal(IxsiMessageType ixsi) {
        log.trace("Entered marshal...");

        try {
            JAXBElement<IxsiMessageType> outgoing = OBJECT_FACTORY.createIxsi(ixsi);
            Marshaller m = jaxbContext.createMarshaller();
            // Validate against the schema
            m.setSchema(schema);
            // Pretty print?
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            // Drop the XML declaration?
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            StringWriter stringWriter = new StringWriter();
            m.marshal(outgoing, stringWriter);
            return stringWriter.toString();

        } catch (JAXBException e) {
            throw new IxsiProcessingException("Could not marshal outgoing message: " + ixsi.toString(),  e);
        }
    }
}
