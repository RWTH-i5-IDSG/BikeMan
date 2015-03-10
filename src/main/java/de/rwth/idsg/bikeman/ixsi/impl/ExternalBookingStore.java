package de.rwth.idsg.bikeman.ixsi.impl;

import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by max on 25/02/15.
 */
@Component
public class ExternalBookingStore extends AbstractSubscriptionStore<UserInfoType> {

    @PostConstruct
    public void init() {
        log.trace("Ready");
    }

    @PreDestroy
    public void preDestroy() {
        log.debug("ExternalBookingStore is being destroyed");
        super.shutDownExecutor();
        // TODO Publish info about going down
    }

}
