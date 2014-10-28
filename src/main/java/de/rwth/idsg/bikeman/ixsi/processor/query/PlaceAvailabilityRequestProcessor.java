package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilityResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.PlaceAvailabilityResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
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
    @Autowired private DatatypeFactory factory;

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
            PlaceIDType placeIDType = new PlaceIDType();
            placeIDType.setValue(String.valueOf(plavdto.getStationId()));
            providerPlaceIDType.setPlaceID(placeIDType);
            ProviderIDType providerIDType = new ProviderIDType();
            providerIDType.setValue(IXSIConstants.Provider.id);
            providerPlaceIDType.setProviderID(providerIDType);
            // Availability
            NonNegativeInteger availability = new NonNegativeInteger();
            availability.setValue(BigInteger.valueOf(plavdto.getAvailableSlots()));

            PlaceAvailabilityType placeAvailabilityType = new PlaceAvailabilityType();
            placeAvailabilityType.setID(providerPlaceIDType);
            placeAvailabilityType.setAvailability(availability);

            placeAvailList.add(placeAvailabilityType);
        }

        PlaceAvailabilityResponseType response = new PlaceAvailabilityResponseType();
        response.getPlace().addAll(placeAvailList);

        UserResponseParams<PlaceAvailabilityResponseType> u = new UserResponseParams<>();
        u.setResponse(response);

        SessionIDType sesId = new SessionIDType();
        sesId.setValue("hello-from-server");
        Duration d = factory.newDuration(5656L);

        u.setSessionID(sesId);
        u.setSessionTimeout(d);

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