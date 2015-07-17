package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import de.rwth.idsg.bikeman.ixsi.impl.ConsumptionStore;
import lombok.extern.slf4j.Slf4j;
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

    private static final TextType DESCRIPTION = new TextType().withLanguage(IXSIConstants.DEFAULT_LANGUAGE)
                                                              .withText("Pedelec Ausleih von Velocity");

    public void report(Booking booking, Transaction transaction) {
        String bookingIdSTR = booking.getIxsiBookingId();
        Set<String> systemIdSet = consumptionStore.getSubscribedSystems(bookingIdSTR);
        if (systemIdSet.isEmpty()) {
            log.debug("Will not push. There is no subscribed system for bookingId '{}'", booking.getBookingId());
            return;
        }

        ConsumptionType consumption = createConsumption(booking, transaction);

        ConsumptionPushMessageType c = new ConsumptionPushMessageType().withConsumption(consumption);
        SubscriptionMessageType sub = new SubscriptionMessageType().withPushMessageGroup(c);
        IxsiMessageType ixsi = new IxsiMessageType().withSubscriptionMessage(sub);

        producer.send(ixsi, systemIdSet);
    }

    public ConsumptionType createConsumption(Booking booking, Transaction t) {
        LocalDateTime start = t.getStartDateTime();
        LocalDateTime end = t.getEndDateTime();

        TimePeriodType timePeriod = new TimePeriodType()
            .withBegin(start.toDateTime())
            .withEnd(end.toDateTime());

        return new ConsumptionType()
            .withBookingID(booking.getIxsiBookingId())
            .withType(IXSIConstants.consumptionClass)
            .withDescription(DESCRIPTION)
            .withTimePeriod(timePeriod);
    }
}
