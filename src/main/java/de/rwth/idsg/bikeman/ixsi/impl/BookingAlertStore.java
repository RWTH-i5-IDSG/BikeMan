package de.rwth.idsg.bikeman.ixsi.impl;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Key : Booking ID
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2015
 */
@Service
public class BookingAlertStore extends AbstractSubscriptionStore<String> {

    @PostConstruct
    public void init() {
        log.trace("Ready");
    }

    @PreDestroy
    public void preDestroy() {
        log.debug("BookingAlertStore is being destroyed");
        super.shutDownExecutor();
        // TODO Publish info about going down
    }

}
