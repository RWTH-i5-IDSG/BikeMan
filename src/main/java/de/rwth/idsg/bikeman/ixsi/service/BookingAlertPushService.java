package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import de.rwth.idsg.bikeman.ixsi.impl.BookingAlertStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xjc.schema.ixsi.BookingAlertPushMessageType;
import xjc.schema.ixsi.BookingChangeType;
import xjc.schema.ixsi.IxsiMessageType;
import xjc.schema.ixsi.SubscriptionMessageType;
import xjc.schema.ixsi.TextType;

import java.util.Set;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.11.2014
 */
@Slf4j
@Service
public class BookingAlertPushService {

    @Autowired private BookingAlertStore bookingAlertStore;
    @Autowired private Producer producer;

    private static final TextType NOT_USED = new TextType().withLanguage(IXSIConstants.DEFAULT_LANGUAGE)
                                                           .withText("Das gebuchte Pedelec wurde nicht benutzt");

    public void alertNotUsed(String ixsiBookingId) {
        Set<String> systemIdSet = bookingAlertStore.getSubscribedSystems(ixsiBookingId);
        if (systemIdSet.isEmpty()) {
            log.debug("Will not push. There is no subscribed system for ixsiBookingId '{}'", ixsiBookingId);
            return;
        }

        BookingChangeType bc = new BookingChangeType()
                .withBookingID(ixsiBookingId)
                .withNotification(true)
                .withReason(NOT_USED);

        proceed(bc, systemIdSet);
    }

    private void proceed(BookingChangeType bc, Set<String> systemIdSet) {
        BookingAlertPushMessageType c = new BookingAlertPushMessageType().withBookingChange(bc);
        SubscriptionMessageType sub = new SubscriptionMessageType().withPushMessageGroup(c);
        IxsiMessageType ixsi = new IxsiMessageType().withSubscriptionMessage(sub);

        producer.send(ixsi, systemIdSet);
    }
}
