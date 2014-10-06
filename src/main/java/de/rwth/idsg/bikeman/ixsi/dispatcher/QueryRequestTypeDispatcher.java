package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import de.rwth.idsg.bikeman.ixsi.processor.query.*;
import de.rwth.idsg.bikeman.ixsi.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @Autowired
    private BookingTargetsInfoRequestProcessor bookingTargetsInfoRequestProcessor;
    @Autowired
    private ChangedProvidersRequestProcessor changedProvidersRequestProcessor;
    @Autowired
    private OpenSessionRequestProcessor openSessionRequestProcessor;
    @Autowired
    private CloseSessionRequestProcessor closeSessionRequestProcessor;
    @Autowired
    private TokenGenerationRequestProcessor tokenGenerationRequestProcessor;
    @Autowired
    private AvailabilityRequestProcessor availabilityRequestProcessor;
    @Autowired
    private PlaceAvailabilityRequestProcessor placeAvailabilityRequestProcessor;
    @Autowired
    private PriceInformationRequestProcessor priceInformationRequestProcessor;
    @Autowired
    private BookingRequestProcessor bookingRequestProcessor;
    @Autowired
    private ChangeBookingRequestProcessor changeBookingRequestProcessor;

    private static DatatypeFactory factory;

    @PostConstruct
    public void initProcessors() throws DatatypeConfigurationException {
        map = new HashMap<>();
        map.put(BookingTargetsInfoRequestType.class, bookingTargetsInfoRequestProcessor);
        map.put(ChangedProvidersRequestType.class, changedProvidersRequestProcessor);
        map.put(OpenSessionRequestType.class, openSessionRequestProcessor);
        map.put(CloseSessionRequestType.class, closeSessionRequestProcessor);
        map.put(TokenGenerationRequestType.class, tokenGenerationRequestProcessor);
        map.put(AvailabilityRequestType.class, availabilityRequestProcessor);
        map.put(PlaceAvailabilityRequestType.class, placeAvailabilityRequestProcessor);
        map.put(PriceInformationRequestType.class, priceInformationRequestProcessor);
        map.put(BookingRequestType.class, bookingRequestProcessor);
        map.put(ChangeBookingRequestType.class, changeBookingRequestProcessor);

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
