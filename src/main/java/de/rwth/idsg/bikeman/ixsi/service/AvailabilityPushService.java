package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import de.rwth.idsg.bikeman.ixsi.impl.AvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilityPushMessageType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetChangeAvailabilityType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetIDType;
import de.rwth.idsg.bikeman.ixsi.schema.IxsiMessageType;
import de.rwth.idsg.bikeman.ixsi.schema.SubscriptionMessageType;
import de.rwth.idsg.bikeman.ixsi.schema.TimePeriodType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 10.11.2014
 */
@Slf4j
@Service
public class AvailabilityPushService {

    @Autowired private Producer producer;
    @Autowired private AvailabilityStore availabilityStore;

    public void takenFromPlace(String bookeeID, String placeID, DateTime departure) {
        buildAndSend(bookeeID, placeID, departure, true);
    }

    public void arrivedAtPlace(String bookeeID, String placeID, DateTime arrival) {
        buildAndSend(bookeeID, placeID, arrival, false);
    }

    private void buildAndSend(String bookeeID, String placeID, DateTime dt, boolean isAvailable) {
        Set<String> systemIdSet = availabilityStore.getSubscribedSystems(bookeeID);
        if (systemIdSet.isEmpty()) {
            log.debug("Will not push. There is no subscribed system for bookeeID '{}'", bookeeID);
            return;
        }

        BookingTargetIDType bookingTargetID = new BookingTargetIDType();
        bookingTargetID.setBookeeID(bookeeID);
        bookingTargetID.setProviderID(IXSIConstants.Provider.id);

        TimePeriodType period = new TimePeriodType();
        period.setBegin(dt);
        period.setEnd(new DateTime().plusHours(6)); // TODO: Does not make sense at all. Find a solution!

        BookingTargetChangeAvailabilityType targetChange = new BookingTargetChangeAvailabilityType();
        targetChange.setID(bookingTargetID);
        targetChange.setPlaceID(placeID);

        if (isAvailable) {
            targetChange.setAvailability(period);
        } else {
            targetChange.setInavailability(period);
        }

        AvailabilityPushMessageType avail = new AvailabilityPushMessageType();
        avail.getAvailabilityChange().add(targetChange);

        SubscriptionMessageType sub = new SubscriptionMessageType();
        sub.setPushMessageGroup(avail);

        IxsiMessageType ixsi = new IxsiMessageType();
        ixsi.setSubscriptionMessage(sub);

        producer.send(ixsi, systemIdSet);
    }

}
