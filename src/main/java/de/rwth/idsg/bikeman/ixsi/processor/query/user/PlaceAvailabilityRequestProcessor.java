package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.PlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.ErrorType;
import xjc.schema.ixsi.Language;
import xjc.schema.ixsi.PlaceAvailabilityRequestType;
import xjc.schema.ixsi.PlaceAvailabilityResponseType;
import xjc.schema.ixsi.PlaceAvailabilityType;
import xjc.schema.ixsi.ProviderPlaceIDType;
import xjc.schema.ixsi.UserInfoType;

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

        return new PlaceAvailabilityResponseType().withPlace(getPlaceAvailabilities(dtos));
    }

    @Override
    public PlaceAvailabilityResponseType processForUser(PlaceAvailabilityRequestType request, Optional<Language> lan,
                                                        UserInfoType userInfo) {
        // TODO
        return buildError(ErrorFactory.Sys.notImplemented(null, null));
    }

    public List<PlaceAvailabilityType> getPlaceAvailabilities(List<PlaceAvailabilityResponseDTO> dtoList) {
        List<PlaceAvailabilityType> placeAvailList = new ArrayList<>();
        for (PlaceAvailabilityResponseDTO dto : dtoList) {

            ProviderPlaceIDType providerPlaceIDType = new ProviderPlaceIDType()
                    .withPlaceID(dto.getManufacturerId())
                    .withProviderID(IXSIConstants.Provider.id);

            placeAvailList.add(new PlaceAvailabilityType()
                    .withID(providerPlaceIDType)
                    .withAvailability(dto.getAvailableSlots()));
        }
        return placeAvailList;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public PlaceAvailabilityResponseType buildError(ErrorType e) {
        return new PlaceAvailabilityResponseType().withError(e);
    }
}
