package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.processor.TokenValidator;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
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
    @Autowired private TokenValidator tokenValidator;

    @Override
    public AvailabilityResponseType processAnonymously(AvailabilityRequestType request, Optional<Language> lan) {

        if (request.isSetBookingTarget()) {
            // we don't want to use booking targets in this request
            return buildError(ErrorFactory.invalidRequest("Cannot use booking targets", null));
        }

        List<AvailabilityResponseDTO> dtos = new ArrayList<>();
        if (request.isSetGeoRectangle()) {
            // handle the request based on a rectangular region
            dtos = queryIXSIRepository.availability(request.getGeoRectangle());
        } else if (request.isSetCircle()) {
            // handle the request based on a circular region
            dtos = queryIXSIRepository.availability(request.getCircle());
        }

        List<BookingTargetAvailabilityType> availabilityList = getBookingTargetAvailabilities(dtos);


        AvailabilityResponseType a = new AvailabilityResponseType();
        a.getBookingTarget().addAll(availabilityList);
        return a;
    }

    /**
     * This method has to validate the user infos !!!!
     */
    @Override
    public AvailabilityResponseType processForUser(AvailabilityRequestType request, Optional<Language> lan,
                                                   List<UserInfoType> userInfoList) {

        AvailabilityResponseType availabilityResponse = new AvailabilityResponseType();
        TokenValidator.Results results = tokenValidator.validate(userInfoList);

        List<ErrorType> errors = results.getErrors();
        if (!errors.isEmpty()) {
            availabilityResponse.getError().addAll(errors);
        }

        List<UserInfoType> validUsers = results.getValidUsers();
        // TODO process with validUsers

        return null;
    }

    public List<BookingTargetAvailabilityType> getBookingTargetAvailabilities(List<AvailabilityResponseDTO> dtos) {
        List<BookingTargetAvailabilityType> availabilityList = new ArrayList<>();
        for (AvailabilityResponseDTO ardto : dtos) {
            // BookingTargetId
            BookingTargetIDType bookingTargetIDType = new BookingTargetIDType();
            String bookeeId = String.valueOf(ardto.getManufacturerId());
            bookingTargetIDType.setBookeeID(bookeeId);
            bookingTargetIDType.setProviderID(IXSIConstants.Provider.id);
            // PlaceID
            String placeId = String.valueOf(ardto.getStationManufacturerId());
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

            bType.setCurrentStateOfCharge(percentType);

            // TODO get the actual driving range from pedelec!
            bType.setCurrentDrivingRange(0);

            availabilityList.add(bType);
        }
        return availabilityList;
    }

    private int roundPercent(Float decimal) {
        return Math.round(decimal * 100);
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public AvailabilityResponseType buildError(ErrorType e) {
        AvailabilityResponseType res = new AvailabilityResponseType();
        res.getError().add(e);
        return res;
    }

}