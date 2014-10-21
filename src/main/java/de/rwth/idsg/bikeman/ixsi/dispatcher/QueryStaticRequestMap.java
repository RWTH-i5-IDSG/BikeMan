package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.processor.query.StaticRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.BookingTargetsInfoRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.ChangedProvidersRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetsInfoRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangedProvidersRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.StaticDataRequestGroup;
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
public class QueryStaticRequestMap extends HashMap<Class<?>, StaticRequestProcessor> {
    private static final long serialVersionUID = 1645602979722968298L;

    @Autowired private BookingTargetsInfoRequestProcessor bookingTargetsInfoRequestProcessor;
    @Autowired private ChangedProvidersRequestProcessor changedProvidersRequestProcessor;

    public QueryStaticRequestMap() {
        super();
        log.trace("Initialized");
    }

    @PostConstruct
    public void init() {
        super.put(BookingTargetsInfoRequestType.class, bookingTargetsInfoRequestProcessor);
        super.put(ChangedProvidersRequestType.class, changedProvidersRequestProcessor);
        log.trace("Ready");
    }

    public StaticRequestProcessor find(StaticDataRequestGroup s) {
        Class<?> clazz = s.getClass();
        StaticRequestProcessor p = super.get(clazz);
        if (p == null) {
            throw new IllegalArgumentException("No processor is registered for the incoming request of type: " + clazz);
        } else {
            return p;
        }
    }
}