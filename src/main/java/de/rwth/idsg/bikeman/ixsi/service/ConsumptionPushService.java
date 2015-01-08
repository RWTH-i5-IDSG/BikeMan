package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.domain.Transaction;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import de.rwth.idsg.bikeman.ixsi.processor.ConsumptionStore;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionPushMessageType;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionType;
import de.rwth.idsg.bikeman.ixsi.schema.IxsiMessageType;
import de.rwth.idsg.bikeman.ixsi.schema.SubscriptionMessageType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.joda.time.Seconds;
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

    private static final String unit = "seconds";
    private static final String description = "Name field contains the booking id";

    public void report(Booking booking) {
        String bookingId = String.valueOf(booking.getBookingId());
        Set<String> systemIdSet = consumptionStore.getSubscribedSystems(bookingId);
        if (systemIdSet.isEmpty()) {
            log.debug("Will not push. There is no subscribed system for bookingId '{}'", bookingId);
            return;
        }

        Transaction t = booking.getTransaction();
        LocalDateTime start = t.getStartDateTime();
        LocalDateTime end = t.getEndDateTime();
        int seconds = Seconds.secondsBetween(start, end).getSeconds();

        ConsumptionType consumption = new ConsumptionType();
        consumption.setType(IXSIConstants.consumptionClass);
        consumption.setUnit(unit);
        consumption.setDescription(description);
        consumption.setName(bookingId);
        consumption.setValue(String.valueOf(seconds));

        ConsumptionPushMessageType c = new ConsumptionPushMessageType();
        c.getConumption().add(consumption);

        SubscriptionMessageType sub = new SubscriptionMessageType();
        sub.setPushMessageGroup(c);

        IxsiMessageType ixsi = new IxsiMessageType();
        ixsi.setSubscriptionMessage(sub);

        producer.send(ixsi, systemIdSet);
    }
}
