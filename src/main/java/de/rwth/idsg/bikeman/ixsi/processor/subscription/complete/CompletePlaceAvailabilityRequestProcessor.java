package de.rwth.idsg.bikeman.ixsi.processor.subscription.complete;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.dto.query.PlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.impl.PlaceAvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestMessageProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.query.user.PlaceAvailabilityRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.CompletePlaceAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompletePlaceAvailabilityResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class CompletePlaceAvailabilityRequestProcessor implements
        SubscriptionRequestMessageProcessor<CompletePlaceAvailabilityRequestType, CompletePlaceAvailabilityResponseType> {

    @Autowired private PlaceAvailabilityStore placeAvailabilityStore;
    @Autowired private QueryIXSIRepository queryIXSIRepository;
    @Autowired private PlaceAvailabilityRequestProcessor placeAvailabilityRequestProcessor;

    @Override
    public CompletePlaceAvailabilityResponseType process(CompletePlaceAvailabilityRequestType request, String systemId) {
        try {
            List<String> ids = placeAvailabilityStore.getSubscriptions(systemId);
            if (ids.isEmpty()) {
                return buildError(ErrorFactory.Sys.invalidRequest("No subscriptions", null));
            }

            List<PlaceAvailabilityResponseDTO> dtos = queryIXSIRepository.placeAvailability(ids);
            List<PlaceAvailabilityType> availabilities = placeAvailabilityRequestProcessor.getPlaceAvailabilities(dtos);

            // for now, assume that client system is always able to process the full message
            // therefore do not split messages!
            return new CompletePlaceAvailabilityResponseType()
                    .withLast(true)
                    .withMessageBlockID(String.valueOf(request.hashCode()))
                    .withPlaceAvailability(availabilities);

        } catch (Exception e) {
            return buildError(ErrorFactory.Sys.backendFailed(e.getMessage(), null));
        }
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public CompletePlaceAvailabilityResponseType buildError(ErrorType e) {
        return new CompletePlaceAvailabilityResponseType().withError(e);
    }
}
