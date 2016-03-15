package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.endpoint.Producer;
import de.rwth.idsg.bikeman.ixsi.store.ConsumptionStore;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xjc.schema.ixsi.ConsumptionPushMessageType;
import xjc.schema.ixsi.ConsumptionType;
import xjc.schema.ixsi.IxsiMessageType;
import xjc.schema.ixsi.SubscriptionMessageType;
import xjc.schema.ixsi.TextType;
import xjc.schema.ixsi.TimePeriodType;

import java.util.Set;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2014
 */
@Slf4j
@Service
public class ConsumptionPushService {

    @Autowired private Producer producer;
    @Autowired private ConsumptionStore consumptionStore;

    private static final TextType USED = new TextType().withLanguage(IXSIConstants.DEFAULT_LANGUAGE)
                                                       .withText("Pedelec Ausleih von Velocity");

    private static final TextType NOT_USED = new TextType().withLanguage(IXSIConstants.DEFAULT_LANGUAGE)
                                                           .withText("Das gebuchte Pedelec wurde nicht benutzt");

    // -------------------------------------------------------------------------
    // USED
    // -------------------------------------------------------------------------

    public void report(Booking booking) {
        Set<String> systemIdSet = consumptionStore.getSubscribedSystems(booking.getIxsiBookingId());
        if (systemIdSet.isEmpty()) {
            log.debug("Will not push. There is no subscribed system for bookingId '{}'", booking.getBookingId());
            return;
        }

        proceed(createConsumption(booking), systemIdSet);
    }

    public ConsumptionType createConsumption(Booking booking) {
        Transaction t = booking.getTransaction();

        LocalDateTime start = t.getStartDateTime();
        LocalDateTime end = t.getEndDateTime();

        TimePeriodType timePeriod = new TimePeriodType()
                .withBegin(start.toDateTime())
                .withEnd(end.toDateTime());

        return new ConsumptionType()
                .withBookingID(booking.getIxsiBookingId())
                .withType(IXSIConstants.consumptionClass)
                .withDescription(USED)
                .withFinal(true)
                .withTimePeriod(timePeriod);
    }

    // -------------------------------------------------------------------------
    // NOT USED
    // -------------------------------------------------------------------------

    public void reportNotUsed(String ixsiBookingId, DateTime reservationEnd) {
        Set<String> systemIdSet = consumptionStore.getSubscribedSystems(ixsiBookingId);
        if (systemIdSet.isEmpty()) {
            log.debug("Will not push. There is no subscribed system for ixsiBookingId '{}'", ixsiBookingId);
            return;
        }

        proceed(createEmptyConsumption(ixsiBookingId, reservationEnd), systemIdSet);
    }

    public ConsumptionType createEmptyConsumption(String ixsiBookingId, DateTime reservationEnd) {

        // Important! Both are set to the same value
        TimePeriodType timePeriod = new TimePeriodType()
                .withBegin(reservationEnd)
                .withEnd(reservationEnd);

        return new ConsumptionType()
                .withBookingID(ixsiBookingId)
                .withType(IXSIConstants.consumptionClass)
                .withDescription(NOT_USED)
                .withFinal(true)
                .withTimePeriod(timePeriod);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private void proceed(ConsumptionType consumption, Set<String> systemIdSet) {
        ConsumptionPushMessageType c = new ConsumptionPushMessageType().withConsumption(consumption);
        SubscriptionMessageType sub = new SubscriptionMessageType().withPushMessageGroup(c);
        IxsiMessageType ixsi = new IxsiMessageType().withSubscriptionMessage(sub);

        producer.send(ixsi, systemIdSet);
    }
}
