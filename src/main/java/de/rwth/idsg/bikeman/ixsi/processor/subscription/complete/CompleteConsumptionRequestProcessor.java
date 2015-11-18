package de.rwth.idsg.bikeman.ixsi.processor.subscription.complete;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.impl.ConsumptionStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestMessageProcessor;
import de.rwth.idsg.bikeman.ixsi.service.ConsumptionPushService;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.CompleteConsumptionRequestType;
import xjc.schema.ixsi.CompleteConsumptionResponseType;
import xjc.schema.ixsi.ConsumptionType;
import xjc.schema.ixsi.ErrorType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.11.2014
 */
@Component
public class CompleteConsumptionRequestProcessor implements
        SubscriptionRequestMessageProcessor<CompleteConsumptionRequestType, CompleteConsumptionResponseType> {

    @Autowired private ConsumptionStore consumptionStore;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private ConsumptionPushService consumptionPushService;

    @Override
    public CompleteConsumptionResponseType process(CompleteConsumptionRequestType request, String systemId) {
        try {
            List<String> bookingIdListString = consumptionStore.getSubscriptions(systemId);
            if (bookingIdListString.isEmpty()) {
                return buildError(ErrorFactory.Sys.invalidRequest("No subscriptions", null));
            }

            List<ConsumptionType> consumptionList = new ArrayList<>();

            List<Booking> closedList = bookingRepository.findClosedBookings(bookingIdListString);
            for (Booking b : closedList) {
                consumptionList.add(consumptionPushService.createConsumption(b));
            }

            List<Booking> notUsedList = bookingRepository.findNotUsedAndExpiredBookings(bookingIdListString);
            for (Booking b : notUsedList) {
                String ixsiBookingId = b.getIxsiBookingId();
                DateTime dt = b.getReservation().getStartDateTime().toDateTime();
                consumptionList.add(consumptionPushService.createEmptyConsumption(ixsiBookingId, dt));
            }

            // for now, assume that client system is always able to process the full message
            // therefore do not split messages!
            return new CompleteConsumptionResponseType()
                    .withLast(true)
                    .withMessageBlockID("none")
                    .withConsumption(consumptionList);

        } catch (Exception e) {
            return buildError(ErrorFactory.Sys.backendFailed(e.getMessage(), null));
        }
    }

    @Override
    public CompleteConsumptionResponseType buildError(ErrorType e) {
        return new CompleteConsumptionResponseType()
            .withError(e)
            .withMessageBlockID("none");
    }
}
