package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.config.PdfConfiguration;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

/**
 * Service for generating and accessing PDF-Files
 *
 *
 */
@Service
@Slf4j
public class PdfService {
    //private FopFactory fopFactory;

    private TransformerFactory tFactory = TransformerFactory.newInstance();

    @Inject
    private FopFactory fopFactory;

    @Inject
    private CustomerRepository customerRepository;


    /* Test function
    @Transactional(readOnly = true)
    public void testPdf(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, response.getOutputStream());

        Customer customer;
        customer = customerRepository.findOne(userId);

        registrationLetter(customer, fop);
    }
    */

    public void registrationLetter(Customer customer, Fop fop) throws Exception {
        ByteArrayOutputStream tmpStream = new ByteArrayOutputStream();

        JAXBContext context = JAXBContext.newInstance(Customer.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        m.marshal(new JAXBElement<Customer>(
            new QName("", "person"), Customer.class, customer), tmpStream);

        Transformer transformer = tFactory.newTransformer(new StreamSource(
            getClass().getResourceAsStream("/pdfs/transformer/registration.xslt")));

        String xmlFileContents = IOUtils.toString(
            getClass().getResourceAsStream("/pdfs/templates/registration.xml"),
            StandardCharsets.UTF_8);

        xmlFileContents = xmlFileContents.replace("${user}", tmpStream.toString());

        Source src = new StreamSource(new StringReader(xmlFileContents));
        Result res = new SAXResult(fop.getDefaultHandler());

        transformer.transform(src, res);
    }
}
