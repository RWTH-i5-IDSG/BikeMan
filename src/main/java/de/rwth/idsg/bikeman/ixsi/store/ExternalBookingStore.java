package de.rwth.idsg.bikeman.ixsi.store;

import org.springframework.stereotype.Component;
import xjc.schema.ixsi.UserInfoType;

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
