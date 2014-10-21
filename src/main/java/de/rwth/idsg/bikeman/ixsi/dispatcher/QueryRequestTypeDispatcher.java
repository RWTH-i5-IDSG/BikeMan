package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.processor.query.UserResponseParams;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.QueryRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.QueryResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.StaticDataRequestGroup;
import de.rwth.idsg.bikeman.ixsi.schema.StaticDataResponseGroup;
import de.rwth.idsg.bikeman.ixsi.schema.UserTriggeredRequestChoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.09.2014
 */
@Slf4j
@Component
public class QueryRequestTypeDispatcher extends AbstractRequestDispatcher {

    @Autowired private QueryUserRequestMap userRequestMap;
    @Autowired private QueryStaticRequestMap staticRequestMap;
    @Autowired private DatatypeFactory factory;

    @Override
    public void handle(CommunicationContext context) {
        log.trace("Entered handle...");

        List<QueryRequestType> requestList = context.getIncomingIxsi().getRequest();
        List<QueryResponseType> responseList = context.getOutgoingIxsi().getResponse();
        log.trace("QueryRequestType size: {}", requestList.size());

        for (QueryRequestType request : requestList) {
            responseList.add(handle(request));
        }
    }

    private QueryResponseType handle(QueryRequestType request) {
        boolean isAllowed = validateSender(request.getSystemID());
        if (!isAllowed) {
            // TODO: Set an error object and early exit this iteration (or something)
        }

        // -------------------------------------------------------------------------
        // Start processing
        // -------------------------------------------------------------------------

        long startTime = System.currentTimeMillis();
        QueryResponseType response = delegate(request);
        long stopTime = System.currentTimeMillis();

        // -------------------------------------------------------------------------
        // End processing
        // -------------------------------------------------------------------------

        Duration calcTime = factory.newDuration(stopTime - startTime);
        response.setCalcTime(calcTime);
        response.setTransaction(request.getTransaction());
        return response;
    }

    private QueryResponseType delegate(QueryRequestType request) {
        if (request.isSetStaticDataRequestGroup()) {
            return buildStaticResponse(request);

        } else if (request.isSetUserTriggeredRequestChoice()) {
            return buildUserResponse(request);

        } else {
            throw new IllegalArgumentException("Unknown incoming message: " + request);
        }
    }

    // -------------------------------------------------------------------------
    // Choice content
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private QueryResponseType buildStaticResponse(QueryRequestType request) {
        log.trace("Entered buildStaticResponse...");

        StaticDataRequestGroup req = request.getStaticDataRequestGroup();
        StaticDataResponseGroup res = staticRequestMap.find(req).process(req);

        QueryResponseType response = new QueryResponseType();
        response.setStaticDataResponseGroup(res);
        return response;
    }

    private QueryResponseType buildUserResponse(QueryRequestType request) {
        log.trace("Entered buildUserResponse...");

        Language lan = request.getLanguage();
        AuthType auth = request.getAuth();
        UserTriggeredRequestChoice c = request.getUserTriggeredRequestChoice();

        UserResponseParams res = userRequestMap.find(c).process(lan, auth, c);

        QueryResponseType response = new QueryResponseType();
        response.setSessionID(res.getSessionID());
        response.setSessionTimeout(res.getSessionTimeout());
        response.setUserTriggeredResponseGroup(res.getResponse());
        return response;
    }
}
