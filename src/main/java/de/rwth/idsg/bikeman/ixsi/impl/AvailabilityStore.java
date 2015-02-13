package de.rwth.idsg.bikeman.ixsi.impl;

import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetIDType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Key : Pedelec ID / BookingTargetID
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 04.11.2014
 */
@Service
public class AvailabilityStore extends AbstractSubscriptionStore<BookingTargetIDType> {

    @PostConstruct
    public void init() {
        log.trace("Ready");
    }

    @PreDestroy
    public void preDestroy() {
        log.debug("AvailabilityStore is being destroyed");
        super.shutDownExecutor();
        // TODO Publish info about going down
    }

}
