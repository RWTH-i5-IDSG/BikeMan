package de.rwth.idsg.bikeman.ixsi.processor.query.user;

import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.*;
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
    public AvailabilityResponseType processAnonymously(AvailabilityRequestType request, Optional<Language> lan) {

        if (request.isSetBookingTarget()) {
            // we don't want to use booking targets in this request
            return buildError(ErrorFactory.Sys.invalidRequest("Cannot use booking targets", null));
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

        return new AvailabilityResponseType().withBookingTarget(availabilityList);
    }

    @Override
    public AvailabilityResponseType processForUser(AvailabilityRequestType request, Optional<Language> lan,
                                                   UserInfoType userInfo) {
        // TODO
        return buildError(ErrorFactory.Sys.notImplemented(null, null));
    }

    public List<BookingTargetAvailabilityType> getBookingTargetAvailabilities(List<AvailabilityResponseDTO> dtoList) {
        List<BookingTargetAvailabilityType> availabilityList = new ArrayList<>();
        for (AvailabilityResponseDTO dto : dtoList) {

            // BookingTargetId
            String bookeeId = String.valueOf(dto.getManufacturerId());
            BookingTargetIDType bookingTargetIDType = new BookingTargetIDType()
                    .withBookeeID(bookeeId)
                    .withProviderID(IXSIConstants.Provider.id);

            // PlaceID
            String placeId = String.valueOf(dto.getStationManufacturerId());

            PercentType percentType = new PercentType()
                    .withValue(roundPercent(dto.getStateOfCharge()));

            BookingTargetAvailabilityType b = new BookingTargetAvailabilityType()
                    .withID(bookingTargetIDType)
                    .withPlaceID(placeId)
                    .withCurrentStateOfCharge(percentType)
                    .withCurrentDrivingRange(0); // TODO get the actual driving range from pedelec!

            // For geo-location queries
            if (dto.getLocationLatitude() != null && dto.getLocationLongitude() != null ) {
                CoordType coordType = new CoordType()
                        .withLatitude(dto.getLocationLatitude())
                        .withLongitude(dto.getLocationLongitude());

                b.setGeoPosition(new GeoPositionType().withCoord(coordType));
            }
            availabilityList.add(b);
        }
        return availabilityList;
    }

    private int roundPercent(Double decimal) {
        return (int) Math.round(decimal);
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public AvailabilityResponseType buildError(ErrorType e) {
        return new AvailabilityResponseType().withError(e);
    }

}
