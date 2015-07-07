package de.rwth.idsg.bikeman.ixsi.processor.subscription.complete;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.dto.query.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.impl.AvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestMessageProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.user.AvailabilityRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetAvailabilityType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetIDType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompleteAvailabilityResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class CompleteAvailabilityRequestProcessor implements
        SubscriptionRequestMessageProcessor<CompleteAvailabilityRequestType, CompleteAvailabilityResponseType> {

    @Autowired private AvailabilityStore availabilityStore;
    @Autowired private QueryIXSIRepository queryIXSIRepository;
    @Autowired private AvailabilityRequestProcessor availabilityRequestProcessor;

    @Override
    public CompleteAvailabilityResponseType process(CompleteAvailabilityRequestType request, String systemId) {
        try {
            List<BookingTargetIDType> targetIds = availabilityStore.getSubscriptions(systemId);
            if (targetIds.isEmpty()) {
                return buildError(ErrorFactory.Sys.invalidRequest("No subscriptions", null));
            }

            List<AvailabilityResponseDTO> responseDTOs = queryIXSIRepository.availability(targetIds);
            List<BookingTargetAvailabilityType> availabilities = availabilityRequestProcessor.getBookingTargetAvailabilities(responseDTOs);

            // for now, assume that client system is always able to process the full message
            // therefore do not split messages!
            return new CompleteAvailabilityResponseType()
                    .withLast(true)
                    .withMessageBlockID("none")
                    .withBookingTarget(availabilities);

        } catch (Exception e) {
            return buildError(ErrorFactory.Sys.backendFailed(e.getMessage(), null));
        }
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public CompleteAvailabilityResponseType buildError(ErrorType e) {
        return new CompleteAvailabilityResponseType()
            .withError(e)
            .withMessageBlockID("none");
    }

}
