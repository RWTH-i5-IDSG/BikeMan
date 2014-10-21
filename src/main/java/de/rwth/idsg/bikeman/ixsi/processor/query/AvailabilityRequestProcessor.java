package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.schema.AuthType;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilityResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.SessionIDType;
import de.rwth.idsg.bikeman.ixsi.schema.UserTriggeredRequestChoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Slf4j
@Component
public class AvailabilityRequestProcessor implements UserRequestProcessor {

    @Autowired private DatatypeFactory factory;

    @Override
    public UserResponseParams process(Language lan, AuthType auth, UserTriggeredRequestChoice c) {
        log.trace("Entered process");

        UserResponseParams u = new UserResponseParams();

        SessionIDType id = new SessionIDType();
        id.setValue("hello-from-server");

        Duration d = factory.newDuration(5656L);

        u.setSessionID(id);
        u.setSessionTimeout(d);
        u.setResponse(new AvailabilityResponseType());

        return u;
    }
}