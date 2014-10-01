package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import de.rwth.idsg.bikeman.ixsi.processor.query.*;
import de.rwth.idsg.bikeman.ixsi.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import java.util.HashMap;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.09.2014
 */
@Slf4j
@Component
public class QueryRequestTypeDispatcher extends AbstractRequestDispatcher {

    @Autowired private Producer producer;

    private final DatatypeFactory factory;

    public QueryRequestTypeDispatcher() throws DatatypeConfigurationException {
        map = new HashMap<>();
        map.put(BookingTargetsInfoRequestType.class, new BookingTargetsInfoRequestProcessor());
        map.put(ChangedProvidersRequestType.class, new ChangedProvidersRequestProcessor());
        map.put(OpenSessionRequestType.class, new OpenSessionRequestProcessor());
        map.put(CloseSessionRequestType.class, new CloseSessionRequestProcessor());
        map.put(TokenGenerationRequestType.class, new TokenGenerationRequestProcessor());
        map.put(AvailabilityRequestType.class, new AvailabilityRequestProcessor());
        map.put(PlaceAvailabilityRequestType.class, new PlaceAvailabilityRequestProcessor());
        map.put(PriceInformationRequestType.class, new PriceInformationRequestProcessor());
        map.put(BookingRequestType.class, new BookingRequestProcessor());
        map.put(ChangeBookingRequestType.class, new ChangeBookingRequestProcessor());

        factory = DatatypeFactory.newInstance(); // This is expensive to init
        log.debug("Ready");
    }

    @Override
    public void handle(CommunicationContext context) {
        log.trace("Entered handle...");

        List<Object> inMessageList = context.getIncomingIxsi().getMessageList();
        List<Object> outMessageList = context.getOutgoingIxsi().getMessageList();
        log.trace("QueryRequestType size: {}", inMessageList.size());

        // There can be multiple QueryRequestTypes (maxOccurs="unbounded") in a IXSI message
        for (Object message : inMessageList) {
            QueryRequestType requestContainer = (QueryRequestType) message;

            if (!isSystemIdValid(requestContainer.getSystemID())) {
                // TODO: Set an error object and early exit this iteration (or something)
            }

            Object actualRequest = requestContainer.getActualRequest().get(0);

            // -------------------------------------------------------------------------
            // Start processing
            // -------------------------------------------------------------------------

            long startTime = System.currentTimeMillis();

            Object actualResponse = super.delegate(actualRequest);

            long stopTime = System.currentTimeMillis();

            // -------------------------------------------------------------------------
            // End processing
            // -------------------------------------------------------------------------

            Duration calcTime = factory.newDuration(stopTime - startTime);

            QueryResponseType responseContainer = new QueryResponseType();
            responseContainer.setTransaction(requestContainer.getTransaction());
            responseContainer.setCalcTime(calcTime);
            responseContainer.getActualResponse().add(actualResponse);

            outMessageList.add(responseContainer);
        }

        producer.send(context);
    }

    private boolean isSystemIdValid(SystemIDType systemID) {
        // TODO
        return false;
    }
}
