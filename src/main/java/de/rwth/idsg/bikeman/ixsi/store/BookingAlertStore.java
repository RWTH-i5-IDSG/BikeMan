package de.rwth.idsg.bikeman.ixsi.store;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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

}
