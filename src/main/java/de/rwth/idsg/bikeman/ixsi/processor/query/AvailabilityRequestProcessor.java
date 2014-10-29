package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilityRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.AvailabilityResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetAvailabilityType;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetIDType;
import de.rwth.idsg.bikeman.ixsi.schema.CoordType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import de.rwth.idsg.bikeman.ixsi.schema.Language;
import de.rwth.idsg.bikeman.ixsi.schema.PercentType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Slf4j
@Component
public class AvailabilityRequestProcessor implements
        UserRequestProcessor<AvailabilityRequestType, AvailabilityResponseType> {

    @Autowired private QueryIXSIRepository queryIXSIRepository;

    @Override
    public UserResponseParams<AvailabilityResponseType> processAnonymously(AvailabilityRequestType request,
                                                                           Optional<Language> lan) {

        if (request.isSetBookingTarget()) {
            // we don't want to use booking targets in this request
            return buildError(ErrorFactory.requestNotSupported());
        }

        List<AvailabilityResponseDTO> dtos = new ArrayList<>();
        if (request.isSetGeoRectangle()) {
            // handle the request based on a rectangular region
            dtos = queryIXSIRepository.availability(request.getGeoRectangle());
        } else if (request.isSetCircle()) {
            // handle the request based on a circular region
            dtos = queryIXSIRepository.availability(request.getCircle());
        }

        List<BookingTargetAvailabilityType> availabilityList = new ArrayList<>();
        for (AvailabilityResponseDTO ardto : dtos) {
            // BookingTargetId
            BookingTargetIDType bookingTargetIDType = new BookingTargetIDType();
            String bookeeId = String.valueOf(ardto.getPedelecId());
            bookingTargetIDType.setBookeeID(bookeeId);
            bookingTargetIDType.setProviderID(IXSIConstants.Provider.id);
            // PlaceID
            String placeId = String.valueOf(ardto.getStationId());
            // GeoPosition
            CoordType coordType = new CoordType();
            coordType.setLatitude(ardto.getLocationLatitude());
            coordType.setLongitude(ardto.getLocationLongitude());
            // CurrentStateOfCharge
            PercentType percentType = new PercentType();
            percentType.setValue(roundPercent(ardto.getStateOfCharge()));

            BookingTargetAvailabilityType bType = new BookingTargetAvailabilityType();
            bType.setID(bookingTargetIDType);
            bType.setPlaceID(placeId);
            bType.setGeoPosition(coordType);
            bType.setCurrentStateOfCharge(percentType);

            // TODO get the actual driving range from pedelec!
            bType.setCurrentDrivingRange(0);

            availabilityList.add(bType);
        }

        AvailabilityResponseType a = new AvailabilityResponseType();
        a.getBookingTarget().addAll(availabilityList);

        UserResponseParams<AvailabilityResponseType> u = new UserResponseParams<>();
        u.setResponse(a);
        return u;
    }

    private int roundPercent(Float decimal) {
        return Math.round(decimal * 100);
    }

    @Override
    public UserResponseParams<AvailabilityResponseType> processForUser(AvailabilityRequestType request,
                                                                       Optional<Language> lan, UserInfoType userInfo) {
        return null;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public UserResponseParams<AvailabilityResponseType> invalidSystem() {
        return buildError(ErrorFactory.invalidSystem());
    }

    @Override
    public UserResponseParams<AvailabilityResponseType> invalidUserAuth() {
        return buildError(ErrorFactory.invalidUserAuth());
    }

    private UserResponseParams<AvailabilityResponseType> buildError(ErrorType e) {
        AvailabilityResponseType res = new AvailabilityResponseType();
        res.getError().add(e);

        UserResponseParams<AvailabilityResponseType> u = new UserResponseParams<>();
        u.setResponse(res);
        return u;
    }

}