package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.PlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityType;
import de.rwth.idsg.bikeman.ixsi.schema.ProviderPlaceIDType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class PlaceAvailabilityRequestProcessor implements
        UserRequestProcessor<PlaceAvailabilityRequestType, PlaceAvailabilityResponseType> {

    @Autowired private QueryIXSIRepository queryIXSIRepository;

    @Override
    public PlaceAvailabilityResponseType processAnonymously(PlaceAvailabilityRequestType request, Optional<Language> lan) {

        List<PlaceAvailabilityResponseDTO> dtos = new ArrayList<>();
        if (request.isSetPlaceID()) {

            List<String> idList = new ArrayList<>();
            for (ProviderPlaceIDType id : request.getPlaceID()) {
                idList.add(id.getPlaceID());
            }
            dtos = queryIXSIRepository.placeAvailability(idList);

        } else if (request.isSetGeoRectangle()) {
            dtos = queryIXSIRepository.placeAvailability(request.getGeoRectangle());

        } else if (request.isSetCircle()) {
            dtos = queryIXSIRepository.placeAvailability(request.getCircle());
        }

        List<PlaceAvailabilityType> placeAvailList = getPlaceAvailabilities(dtos);


        PlaceAvailabilityResponseType response = new PlaceAvailabilityResponseType()
            .withPlace(placeAvailList);
        return response;
    }

    /**
     * This method has to validate the user infos !!!!
     */
    @Override
    public PlaceAvailabilityResponseType processForUser(PlaceAvailabilityRequestType request, Optional<Language> lan,
                                                        List<UserInfoType> userInfoList) {
        return null;
    }

    public List<PlaceAvailabilityType> getPlaceAvailabilities(List<PlaceAvailabilityResponseDTO> dtos) {
        List<PlaceAvailabilityType> placeAvailList = new ArrayList<>();
        for (PlaceAvailabilityResponseDTO plavdto : dtos) {
            // Id
            ProviderPlaceIDType providerPlaceIDType = new ProviderPlaceIDType()
                .withPlaceID(plavdto.getManufacturerId())
                .withProviderID(IXSIConstants.Provider.id);

            PlaceAvailabilityType placeAvailabilityType = new PlaceAvailabilityType()
                .withID(providerPlaceIDType)
                .withAvailability(plavdto.getAvailableSlots());

            placeAvailList.add(placeAvailabilityType);
        }
        return placeAvailList;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public PlaceAvailabilityResponseType buildError(ErrorType e) {
        return new PlaceAvailabilityResponseType()
            .withError(e);
    }
}
