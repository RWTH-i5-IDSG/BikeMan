package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.SubscriptionRequestMessageProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.subscription.SubscriptionRequestProcessor;
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

        // Process the request
        //
        long startTime = System.currentTimeMillis();
        SubscriptionResponseType response = delegate(request);
        long stopTime = System.currentTimeMillis();

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
            throw new IxsiProcessingException("Incoming message must be from " +
                    "SubscriptionAdministrationRequestGroup, SubscriptionRequestGroup or RequestMessageGroup");
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
        SubscriptionRequestProcessor p = requestMap.find(req);

        // System validation
        //
        SubscriptionResponseGroup res;
        if (systemValidator.validate(request.getSystemID())) {
            res = p.process(req);
        } else {
            res = p.invalidSystem();
        }

        SubscriptionResponseType s = new SubscriptionResponseType();
        s.setSubscriptionResponseGroup(res);
        return s;
    }

    @SuppressWarnings("unchecked")
    private SubscriptionResponseType buildResponseMessage(SubscriptionRequestType request) {
        log.trace("Entered buildResponseMessage...");

        RequestMessageGroup req = request.getRequestMessageGroup();
        SubscriptionRequestMessageProcessor p = requestMessageMap.find(req);

        // System validation
        //
        ResponseMessageGroup res;
        if (systemValidator.validate(request.getSystemID())) {
            res = p.process(req);
        } else {
            res = p.invalidSystem();
        }

        SubscriptionResponseType s = new SubscriptionResponseType();
        s.setResponseMessageGroup(res);
        return s;
    }
}