package de.rwth.idsg.bikeman.ixsi.processor.subscription;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.dto.query.PlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.processor.PlaceAvailabilityStore;
import de.rwth.idsg.bikeman.ixsi.processor.query.PlaceAvailabilityRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.CompletePlaceAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.CompletePlaceAvailabilityResponseType;
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
        List<String> ids = placeAvailabilityStore.getSubscriptions(systemId);

        List<PlaceAvailabilityResponseDTO> dtos = queryIXSIRepository.placeAvailability(ids);
        List<PlaceAvailabilityType> availabilities = placeAvailabilityRequestProcessor.getPlaceAvailabilities(dtos);

        CompletePlaceAvailabilityResponseType response = new CompletePlaceAvailabilityResponseType();
        response.setLast(true);
        response.setMessageBlockID(String.valueOf(request.hashCode()));
        response.getPlaceAvailability().addAll(availabilities);

        return response;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public CompletePlaceAvailabilityResponseType invalidSystem() {
        CompletePlaceAvailabilityResponseType b = new CompletePlaceAvailabilityResponseType();
        b.getError().add(ErrorFactory.invalidSystem());
        return b;
    }
}
