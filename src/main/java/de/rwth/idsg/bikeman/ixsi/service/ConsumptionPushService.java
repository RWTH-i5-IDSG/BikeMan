package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import de.rwth.idsg.bikeman.ixsi.impl.ConsumptionStore;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionPushMessageType;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionType;
import de.rwth.idsg.bikeman.ixsi.schema.IxsiMessageType;
import de.rwth.idsg.bikeman.ixsi.schema.SubscriptionMessageType;
import de.rwth.idsg.bikeman.ixsi.schema.TimePeriodType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public static final String NAME_FORMAT = "The booking with id %s rented a bike from %s to %s.";

    public void report(Long bookingId, Transaction transaction) {
        String bookingIdSTR = String.valueOf(bookingId);
        Set<String> systemIdSet = consumptionStore.getSubscribedSystems(bookingIdSTR);
        if (systemIdSet.isEmpty()) {
            log.debug("Will not push. There is no subscribed system for bookingId '{}'", bookingId);
            return;
        }

        ConsumptionType consumption = createConsumption(bookingIdSTR, transaction);

        ConsumptionPushMessageType c = new ConsumptionPushMessageType().withConsumption(consumption);
        SubscriptionMessageType sub = new SubscriptionMessageType().withPushMessageGroup(c);
        IxsiMessageType ixsi = new IxsiMessageType().withSubscriptionMessage(sub);

        producer.send(ixsi, systemIdSet);
    }

    public ConsumptionType createConsumption(String bookingId, Transaction t) {
        LocalDateTime start = t.getStartDateTime();
        LocalDateTime end = t.getEndDateTime();

        TimePeriodType timePeriod = new TimePeriodType()
            .withBegin(start.toDateTime())
            .withEnd(end.toDateTime());

        return new ConsumptionType()
            .withBookingID(bookingId)
            .withType(IXSIConstants.consumptionClass)
            .withName(String.format(NAME_FORMAT, bookingId, start, end))
            .withTimePeriod(timePeriod);
    }
}
