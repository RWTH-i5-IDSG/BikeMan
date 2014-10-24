package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.schema.HeartBeatResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.RequestMessageGroup;
import de.rwth.idsg.bikeman.ixsi.schema.ResponseMessageGroup;
import de.rwth.idsg.bikeman.ixsi.schema.SubscriptionRequestGroup;
import de.rwth.idsg.bikeman.ixsi.schema.SubscriptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.SubscriptionResponseGroup;
import de.rwth.idsg.bikeman.ixsi.schema.SubscriptionResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.09.2014
 */
@Slf4j
@Component
public class SubscriptionRequestTypeDispatcher implements Dispatcher {

    @Autowired private SubscriptionRequestMap requestMap;
    @Autowired private SubscriptionRequestMessageMap requestMessageMap;
    @Autowired private DatatypeFactory factory;
    @Autowired private SystemValidator systemValidator;

    @Override
    public void handle(CommunicationContext context) {
        log.trace("Entered handle...");

        SubscriptionRequestType request = context.getIncomingIxsi().getSubscriptionRequest();
        SubscriptionResponseType response = handle(request);
        context.getOutgoingIxsi().setSubscriptionResponse(response);
    }

    private SubscriptionResponseType handle(SubscriptionRequestType request) {
        boolean isAllowed = systemValidator.validate(request.getSystemID());
        if (!isAllowed) {
            // TODO: Set an error object and early exit this iteration (or something)
        }

        // -------------------------------------------------------------------------
        // Start processing
        // -------------------------------------------------------------------------

        long startTime = System.currentTimeMillis();
        SubscriptionResponseType response = delegate(request);
        long stopTime = System.currentTimeMillis();

        // -------------------------------------------------------------------------
        // End processing
        // -------------------------------------------------------------------------

        Duration calcTime = factory.newDuration(stopTime - startTime);
        response.setCalcTime(calcTime);
        response.setTransaction(request.getTransaction());
        return response;
    }

    private SubscriptionResponseType delegate(SubscriptionRequestType request) {
        if (request.isSetHeartBeat()) {
            return buildHeartbeat();

        } else if (request.isSetSubscriptionRequestGroup()) {
            return buildResponse(request);

        } else if (request.isSetRequestMessageGroup()) {
            return buildResponseMessage(request);

        } else {
            throw new IxsiProcessingException("Unknown incoming message: " + request);
        }
    }

    // -------------------------------------------------------------------------
    // Choice content
    // -------------------------------------------------------------------------

    private SubscriptionResponseType buildHeartbeat() {
        log.trace("Entered buildHeartbeat...");

        SubscriptionResponseType s = new SubscriptionResponseType();
        s.setHeartBeat(new HeartBeatResponseType());
        return s;
    }

    @SuppressWarnings("unchecked")
    private SubscriptionResponseType buildResponse(SubscriptionRequestType request) {
        log.trace("Entered buildResponse...");

        SubscriptionRequestGroup req = request.getSubscriptionRequestGroup();
        SubscriptionResponseGroup res = requestMap.find(req).process(req);

        SubscriptionResponseType s = new SubscriptionResponseType();
        s.setSubscriptionResponseGroup(res);
        return s;
    }

    @SuppressWarnings("unchecked")
    private SubscriptionResponseType buildResponseMessage(SubscriptionRequestType request) {
        log.trace("Entered buildResponseMessage...");

        RequestMessageGroup req = request.getRequestMessageGroup();
        ResponseMessageGroup res = requestMessageMap.find(req).process(req);

        SubscriptionResponseType s = new SubscriptionResponseType();
        s.setResponseMessageGroup(res);
        return s;
    }
}