package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestMessageProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.repository.SystemValidator;
import de.rwth.idsg.ixsi.jaxb.RequestMessageGroup;
import de.rwth.idsg.ixsi.jaxb.ResponseMessageGroup;
import de.rwth.idsg.ixsi.jaxb.SubscriptionRequestGroup;
import de.rwth.idsg.ixsi.jaxb.SubscriptionResponseGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.HeartBeatResponseType;
import xjc.schema.ixsi.SubscriptionRequestType;
import xjc.schema.ixsi.SubscriptionResponseType;

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

        return new SubscriptionResponseType()
                .withHeartBeat(new HeartBeatResponseType());
    }

    @SuppressWarnings("unchecked")
    private SubscriptionResponseType buildResponse(SubscriptionRequestType request) {
        log.trace("Entered buildResponse...");

        SubscriptionRequestGroup req = request.getSubscriptionRequestGroup();
        SubscriptionRequestProcessor p = requestMap.find(req);

        // System validation
        //
        SubscriptionResponseGroup res;
        String systemID = request.getSystemID();
        if (systemValidator.validate(systemID)) {
            res = p.process(req, systemID);
        } else {
            res = p.buildError(ErrorFactory.Sys.idUknown());
        }

        return new SubscriptionResponseType()
                .withSubscriptionResponseGroup(res);
    }

    @SuppressWarnings("unchecked")
    private SubscriptionResponseType buildResponseMessage(SubscriptionRequestType request) {
        log.trace("Entered buildResponseMessage...");

        RequestMessageGroup req = request.getRequestMessageGroup();
        SubscriptionRequestMessageProcessor p = requestMessageMap.find(req);

        // System validation
        //
        ResponseMessageGroup res;
        String systemID = request.getSystemID();
        if (systemValidator.validate(systemID)) {
            res = p.process(req, systemID);
        } else {
            res = p.buildError(ErrorFactory.Sys.idUknown());
        }

        return new SubscriptionResponseType()
                .withResponseMessageGroup(res);
    }
}
