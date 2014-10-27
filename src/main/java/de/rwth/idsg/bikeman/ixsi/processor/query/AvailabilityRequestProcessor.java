package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.*;
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
public class AvailabilityRequestProcessor implements
        UserRequestProcessor<AvailabilityRequestType, AvailabilityResponseType> {

    @Autowired private DatatypeFactory factory;

    @Override
    public UserResponseParams<AvailabilityResponseType> processAnonymously(AvailabilityRequestType request,
                                                                           Optional<Language> lan) {

        SessionIDType sesId = new SessionIDType();
        sesId.setValue("hello-from-server");

        BookeeIDType bookeeIDType = new BookeeIDType();
        bookeeIDType.setValue("asdfgb");

        ProviderIDType providerIDType = new ProviderIDType();
        providerIDType.setValue("adsf");

        BookingTargetIDType bId = new BookingTargetIDType();
        bId.setBookeeID(bookeeIDType);
        bId.setProviderID(providerIDType);

        BookingTargetAvailabilityType b = new BookingTargetAvailabilityType();
        b.setID(bId);

        AvailabilityResponseType a = new AvailabilityResponseType();
        a.getBookingTarget().add(b);

        Duration d = factory.newDuration(5656L);

        UserResponseParams<AvailabilityResponseType> u = new UserResponseParams<>();
        u.setSessionID(sesId);
        u.setSessionTimeout(d);
        u.setResponse(a);
        return u;
    }

    @Override
    public UserResponseParams<AvailabilityResponseType> processForUser(AvailabilityRequestType request,
                                                                       Optional<Language> lan, UserInfoType userInfo) {
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public UserResponseParams<AvailabilityResponseType> invalidSystem() {
        return buildError(ErrorFactory.invalidSystem());
    }

    @Override
    public UserResponseParams<AvailabilityResponseType> invalidUserAuth() {
        return buildError(ErrorFactory.invalidUserAuth());
    }

    private UserResponseParams<AvailabilityResponseType> buildError(ErrorType e) {
        AvailabilityResponseType res = new AvailabilityResponseType();
        res.getError().add(e);

        UserResponseParams<AvailabilityResponseType> u = new UserResponseParams<>();
        u.setResponse(res);
        return u;
    }

}