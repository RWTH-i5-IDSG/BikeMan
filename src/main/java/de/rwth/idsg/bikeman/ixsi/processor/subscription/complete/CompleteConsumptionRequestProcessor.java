package de.rwth.idsg.bikeman.ixsi.processor.subscription.complete;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.ixsi.impl.ConsumptionStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestMessageProcessor;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteConsumptionRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteConsumptionResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ConsumptionType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.service.ConsumptionPushService;
import de.rwth.idsg.bikeman.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        List<String> bookingIdListString = consumptionStore.getSubscriptions(systemId);

        List<Long> bookingIdListLong = new ArrayList<>();
        for (String str : bookingIdListString) {
            bookingIdListLong.add(Long.valueOf(str));
        }
        List<Booking> bookingList = bookingRepository.findClosedBookings(bookingIdListLong);

        // for now, assume that client system is always able to process the full message
        // therefore do not split messages!
        CompleteConsumptionResponseType response = new CompleteConsumptionResponseType()
            .withLast(true)
            .withMessageBlockID(String.valueOf(request.hashCode()));

        List<ConsumptionType> consumptionList = response.getConsumption();
        for (Booking b : bookingList) {
            String bookingId = String.valueOf(b.getBookingId());
            consumptionList.add(consumptionPushService.createConsumption(bookingId, b.getTransaction()));
        }
        return response;
    }

    @Override
    public CompleteConsumptionResponseType buildError(ErrorType e) {
        return new CompleteConsumptionResponseType()
            .withError(e);
    }
}
