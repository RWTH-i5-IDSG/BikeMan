package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.PlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class PlaceAvailabilityRequestProcessor implements UserRequestProcessor<PlaceAvailabilityRequestType, PlaceAvailabilityResponseType> {

    @Autowired private QueryIXSIRepository queryIXSIRepository;

    @Override
    public UserResponseParams<PlaceAvailabilityResponseType> processAnonymously(PlaceAvailabilityRequestType request, Optional<Language> lan) {

        List<PlaceAvailabilityResponseDTO> dtos = new ArrayList<>();
        if (request.isSetPlaceID()) {
            dtos = queryIXSIRepository.placeAvailability(request.getPlaceID());
        } else if (request.isSetGeoRectangle()) {
            dtos = queryIXSIRepository.placeAvailability(request.getGeoRectangle());
        } else if (request.isSetCircle()) {
            dtos = queryIXSIRepository.placeAvailability(request.getCircle());
        }

        List<PlaceAvailabilityType> placeAvailList = new ArrayList<>();
        for (PlaceAvailabilityResponseDTO plavdto : dtos) {
            // Id
            ProviderPlaceIDType providerPlaceIDType = new ProviderPlaceIDType();
            providerPlaceIDType.setPlaceID(String.valueOf(plavdto.getStationId()));
            providerPlaceIDType.setProviderID(IXSIConstants.Provider.id);

            PlaceAvailabilityType placeAvailabilityType = new PlaceAvailabilityType();
            placeAvailabilityType.setID(providerPlaceIDType);
            placeAvailabilityType.setAvailability(plavdto.getAvailableSlots());

            placeAvailList.add(placeAvailabilityType);
        }

        PlaceAvailabilityResponseType response = new PlaceAvailabilityResponseType();
        response.getPlace().addAll(placeAvailList);

        UserResponseParams<PlaceAvailabilityResponseType> u = new UserResponseParams<>();
        u.setResponse(response);
        return u;
    }

    @Override
    public UserResponseParams<PlaceAvailabilityResponseType> processForUser(PlaceAvailabilityRequestType request, Optional<Language> lan, UserInfoType userInfo) {
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public UserResponseParams<PlaceAvailabilityResponseType> invalidSystem() {
        return buildError(ErrorFactory.invalidSystem());
    }

    @Override
    public UserResponseParams<PlaceAvailabilityResponseType> invalidUserAuth() {
        return buildError(ErrorFactory.invalidUserAuth());
    }

    private UserResponseParams<PlaceAvailabilityResponseType> buildError(ErrorType e) {
        PlaceAvailabilityResponseType res = new PlaceAvailabilityResponseType();
        res.getError().add(e);

        UserResponseParams<PlaceAvailabilityResponseType> u = new UserResponseParams<>();
        u.setResponse(res);
        return u;
    }
}