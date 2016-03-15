package de.rwth.idsg.bikeman.ixsi.store;

import org.springframework.stereotype.Service;
import xjc.schema.ixsi.BookingTargetIDType;

import javax.annotation.PostConstruct;

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
}
