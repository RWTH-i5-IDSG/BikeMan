package de.rwth.idsg.bikeman.ixsi.processor.subscription.complete;

import de.rwth.idsg.bikeman.domain.Booking;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
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
        try {
            List<String> bookingIdListString = consumptionStore.getSubscriptions(systemId);
            List<Booking> bookingList = bookingRepository.findClosedBookings(bookingIdListString);
            List<ConsumptionType> consumptionList = new ArrayList<>();
            for (Booking b : bookingList) {
                consumptionList.add(consumptionPushService.createConsumption(b, b.getTransaction()));
            }

            // for now, assume that client system is always able to process the full message
            // therefore do not split messages!
            return new CompleteConsumptionResponseType()
                    .withLast(true)
                    .withMessageBlockID(String.valueOf(request.hashCode()))
                    .withConsumption(consumptionList);

        } catch (Exception e) {
            return buildError(ErrorFactory.Sys.backendFailed(e.getMessage(), null));
        }
    }

    @Override
    public CompleteConsumptionResponseType buildError(ErrorType e) {
        return new CompleteConsumptionResponseType().withError(e);
    }
}
