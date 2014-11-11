package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.api.Producer;
import de.rwth.idsg.bikeman.ixsi.processor.AvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilityPushMessageType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetChangeAvailabilityType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetIDType;
import de.rwth.idsg.bikeman.ixsi.schema.IxsiMessageType;
import de.rwth.idsg.bikeman.ixsi.schema.SubscriptionMessageType;
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

    private void push(AvailabilityPushMessageType msg, Set<String> systemIdSet) {
        SubscriptionMessageType sub = new SubscriptionMessageType();
        sub.setPushMessageGroup(msg);

        IxsiMessageType ixsi = new IxsiMessageType();
        ixsi.setSubscriptionMessage(sub);

        producer.send(ixsi, systemIdSet);
    }

    public void arrivedAtLocation(String bookeeID, String placeID, DateTime arrival) {
        BookingTargetIDType bookingTargetID = new BookingTargetIDType();
        bookingTargetID.setBookeeID(bookeeID);
        bookingTargetID.setProviderID(IXSIConstants.Provider.id);

        BookingTargetChangeAvailabilityType targetChange = new BookingTargetChangeAvailabilityType();
        targetChange.setID(bookingTargetID);
        targetChange.setPlaceID(placeID);

        // TODO targetChange.setAvailability();

        AvailabilityPushMessageType a = new AvailabilityPushMessageType();
        a.getAvailabilityChange().add(targetChange);
        Set<String> systemIdSet = availabilityStore.getSubscribedSystems(Long.valueOf(bookeeID));

        this.push(a, systemIdSet);
    }

    public void takenFromLocation(BookingTargetIDType id, String placeID, DateTime departure) {

    }
}
