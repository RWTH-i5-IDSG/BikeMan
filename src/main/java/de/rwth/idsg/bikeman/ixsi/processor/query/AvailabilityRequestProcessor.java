package de.rwth.idsg.bikeman.ixsi.processor.query;

import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.*;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class AvailabilityRequestProcessor implements
        UserRequestProcessor<AvailabilityRequestType, AvailabilityResponseType> {

    @Autowired private DatatypeFactory factory;

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
            BookeeIDType bookeeIDType = new BookeeIDType();
            bookeeIDType.setValue(String.valueOf(ardto.getPedelecId()));
            bookingTargetIDType.setBookeeID(bookeeIDType);
            ProviderIDType providerIDType = new ProviderIDType();
            providerIDType.setValue(IXSIConstants.Provider.id);
            bookingTargetIDType.setProviderID(providerIDType);
            // PlaceID
            PlaceIDType placeIDType = new PlaceIDType();
            placeIDType.setValue(String.valueOf(ardto.getStationId()));
            // GeoPosition
            CoordType coordType = new CoordType();
            coordType.setLatitude(ardto.getLocationLatitude());
            coordType.setLongitude(ardto.getLocationLongitude());
            // CurrentStateOfCharge
            PercentType percentType = new PercentType();
            percentType.setValue(roundPercent(ardto.getStateOfCharge()));
            // CurrentDrivingRange
            NonNegativeInteger drivingRange = new NonNegativeInteger();
            // TODO get the actual driving range from pedelec!
            drivingRange.setValue(BigInteger.valueOf(0));

            BookingTargetAvailabilityType bType = new BookingTargetAvailabilityType();
            bType.setID(bookingTargetIDType);
            bType.setPlaceID(placeIDType);
            bType.setGeoPosition(coordType);
            bType.setCurrentStateOfCharge(percentType);
            bType.setCurrentDrivingRange(drivingRange);

            availabilityList.add(bType);
        }

        // TODO make these values real!
        SessionIDType sesId = new SessionIDType();
        sesId.setValue("hello-from-server");
        Duration d = factory.newDuration(5656L);

        AvailabilityResponseType a = new AvailabilityResponseType();
        a.getBookingTarget().addAll(availabilityList);


        UserResponseParams<AvailabilityResponseType> u = new UserResponseParams<>();
        u.setSessionID(sesId);
        u.setSessionTimeout(d);
        u.setResponse(a);
        return u;
    }

    private NonNegativeInteger roundPercent(Float decimal) {
        long percent = Math.round(decimal * 100);
        NonNegativeInteger nonNegativeInteger = new NonNegativeInteger();
        nonNegativeInteger.setValue(BigInteger.valueOf(percent));

        return nonNegativeInteger;
    }

    @Override
    public UserResponseParams<AvailabilityResponseType> processForUser(AvailabilityRequestType request,
                                                                       Optional<Language> lan, UserInfoType userInfo) {
        return null;
    }

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