package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.processor.query.AvailabilityRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.BookingRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.ChangeBookingRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.CloseSessionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.OpenSessionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.PlaceAvailabilityRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.PriceInformationRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.TokenGenerationRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangeBookingRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CloseSessionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.OpenSessionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PriceInformationRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.TokenGenerationRequestType;
import de.rwth.idsg.ixsi.jaxb.UserTriggeredRequestChoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 21.10.2014
 */
@Slf4j
@Component
public class QueryUserRequestMap extends HashMap<Class<?>, UserRequestProcessor> {
    private static final long serialVersionUID = -2498351236190286177L;

    @Autowired private OpenSessionRequestProcessor openSessionRequestProcessor;
    @Autowired private CloseSessionRequestProcessor closeSessionRequestProcessor;
    @Autowired private TokenGenerationRequestProcessor tokenGenerationRequestProcessor;
    @Autowired private AvailabilityRequestProcessor availabilityRequestProcessor;
    @Autowired private PlaceAvailabilityRequestProcessor placeAvailabilityRequestProcessor;
    @Autowired private PriceInformationRequestProcessor priceInformationRequestProcessor;
    @Autowired private BookingRequestProcessor bookingRequestProcessor;
    @Autowired private ChangeBookingRequestProcessor changeBookingRequestProcessor;

    public QueryUserRequestMap() {
        super();
        log.trace("Initialized");
    }

    @PostConstruct
    public void init() {
        super.put(OpenSessionRequestType.class, openSessionRequestProcessor);
        super.put(CloseSessionRequestType.class, closeSessionRequestProcessor);
        super.put(TokenGenerationRequestType.class, tokenGenerationRequestProcessor);
        super.put(AvailabilityRequestType.class, availabilityRequestProcessor);
        super.put(PlaceAvailabilityRequestType.class, placeAvailabilityRequestProcessor);
        super.put(PriceInformationRequestType.class, priceInformationRequestProcessor);
        super.put(BookingRequestType.class, bookingRequestProcessor);
        super.put(ChangeBookingRequestType.class, changeBookingRequestProcessor);
        log.trace("Ready");
    }

    public UserRequestProcessor find(UserTriggeredRequestChoice c) {
        Class<?> clazz = c.getClass();
        UserRequestProcessor p = super.get(clazz);
        if (p == null) {
            throw new IxsiProcessingException("No processor is registered for the incoming request of type: " + clazz);
        } else {
            return p;
        }
    }
}