package de.rwth.idsg.bikeman.ixsi.dispatcher;

import de.rwth.idsg.bikeman.ixsi.CommunicationContext;
import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.09.2014
 */
@Slf4j
public abstract class AbstractRequestDispatcher implements Dispatcher {

    Map<Class, Processor> map;

    @Override
    public void handle(CommunicationContext context) {
        // Override in extending classes
    }

    @SuppressWarnings("unchecked")
    Object delegate(Object request) {
        Class<?> clazz = request.getClass();
        Processor p = map.get(clazz);
        if (p == null) {
            throw new IllegalArgumentException("No processor is registered for the incoming request of type: " + clazz);
        } else {
            log.trace("Processing class: {}", clazz);
            return p.process(request);
        }
    }
}