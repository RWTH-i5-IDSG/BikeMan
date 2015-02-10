package de.rwth.idsg.bikeman.ixsi.dispatcher;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.processor.api.StaticRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.repository.SystemValidator;
import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.QueryRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.QueryResponseType;
import de.rwth.idsg.ixsi.jaxb.StaticDataRequestGroup;
import de.rwth.idsg.ixsi.jaxb.StaticDataResponseGroup;
import de.rwth.idsg.ixsi.jaxb.UserTriggeredRequestChoice;
import de.rwth.idsg.ixsi.jaxb.UserTriggeredResponseChoice;
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
public class QueryRequestTypeDispatcher implements Dispatcher {

    @Autowired private QueryUserRequestMap userRequestMap;
    @Autowired private QueryStaticRequestMap staticRequestMap;
    @Autowired private DatatypeFactory factory;
    @Autowired private SystemValidator systemValidator;

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

        // Process the request
        //
        long startTime = System.currentTimeMillis();
        QueryResponseType response = delegate(request);
        long stopTime = System.currentTimeMillis();

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
            throw new IxsiProcessingException("Unknown incoming message: " + request);
        }
    }

    // -------------------------------------------------------------------------
    // Choice content
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private QueryResponseType buildStaticResponse(QueryRequestType request) {
        log.trace("Entered buildStaticResponse...");

        StaticDataRequestGroup req = request.getStaticDataRequestGroup();
        StaticRequestProcessor p = staticRequestMap.find(req);

        // System validation
        //
        StaticDataResponseGroup res;
        if (systemValidator.validate(request.getSystemID())) {
            res = p.process(req);
        } else {
            res = p.buildError(ErrorFactory.invalidSystem());
        }

        return new QueryResponseType()
                .withStaticDataResponseGroup(res);
    }

    @SuppressWarnings("unchecked")
    private QueryResponseType buildUserResponse(QueryRequestType request) {
        log.trace("Entered buildUserResponse...");

        UserTriggeredRequestChoice c = request.getUserTriggeredRequestChoice();
        UserRequestProcessor p = userRequestMap.find(c);

        // System validation
        //
        UserTriggeredResponseChoice responseChoice;
        if (systemValidator.validate(request.getSystemID())) {
            responseChoice = delegateUserRequest(c, p, request.getAuth(), Optional.fromNullable(request.getLanguage()));
        } else {
            responseChoice = p.buildError(ErrorFactory.invalidSystem());
        }

        return new QueryResponseType()
                .withUserTriggeredResponseGroup(responseChoice);
    }

    @SuppressWarnings("unchecked")
    private UserTriggeredResponseChoice delegateUserRequest(UserTriggeredRequestChoice c, UserRequestProcessor p,
                                                            AuthType auth, Optional<Language> lan) {
        log.trace("Processing the authentication information...");

        if (auth.isSetAnonymous() && auth.isAnonymous()) {
            return p.processAnonymously(c, lan);

        } else if (auth.isSetUserInfo()) {
            return p.processForUser(c, lan, auth.getUserInfo());

        } else if (auth.isSetSessionID()) {
            return p.buildError(ErrorFactory.notImplemented("Session-based authentication is not supported", null));

        } else {
            return p.buildError(ErrorFactory.invalidRequest("Authentication requirements are not met", null));
        }
    }
}
