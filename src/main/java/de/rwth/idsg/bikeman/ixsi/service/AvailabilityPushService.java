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

    /**
     * @param bookeeID      Manufacturer ID of the pedelec.
     * @param placeID       Manufacturer ID of the station.
     * @param departure     Date/time of the start of the transaction.
     */
    public void takenFromPlace(String bookeeID, String placeID, DateTime departure) {
        buildAndSend(bookeeID, placeID, departure, false);
    }

    /**
     * @param bookeeID      Manufacturer ID of the pedelec.
     * @param placeID       Manufacturer ID of the station.
     * @param departure     Date/time of the start of the transaction. This is rightfully so and not the date/time
     *                      of the arrival, because in client system we want to invalidate the time period that
     *                      was sent earlier with {@link #takenFromPlace(String, String, org.joda.time.DateTime)}.
     *                      Therefore, the two values have to match.
     */
    public void arrivedAtPlace(String bookeeID, String placeID, DateTime departure) {
        buildAndSend(bookeeID, placeID, departure, true);
    }

    private void buildAndSend(String bookeeID, String placeID, DateTime dt, boolean isAvailable) {
        BookingTargetIDType bookingTargetID = new BookingTargetIDType()
                .withBookeeID(bookeeID)
                .withProviderID(IXSIConstants.Provider.id);

        Set<String> systemIdSet = availabilityStore.getSubscribedSystems(bookingTargetID);
        if (systemIdSet.isEmpty()) {
            log.debug("Will not push. There is no subscribed system for bookeeID '{}'", bookeeID);
            return;
        }

        TimePeriodType period = new TimePeriodType()
                .withBegin(dt)
                .withEnd(dt.plusHours(6)); // TODO: Does not make sense at all. Find a solution!

        BookingTargetChangeAvailabilityType targetChange = new BookingTargetChangeAvailabilityType()
                .withID(bookingTargetID)
                .withPlaceID(placeID);

        if (isAvailable) {
            targetChange.setAvailability(period);
        } else {
            targetChange.setInavailability(period);
        }

        AvailabilityPushMessageType avail = new AvailabilityPushMessageType().withAvailabilityChange(targetChange);
        SubscriptionMessageType sub = new SubscriptionMessageType().withPushMessageGroup(avail);
        IxsiMessageType ixsi = new IxsiMessageType().withSubscriptionMessage(sub);

        producer.send(ixsi, systemIdSet);
    }

}
