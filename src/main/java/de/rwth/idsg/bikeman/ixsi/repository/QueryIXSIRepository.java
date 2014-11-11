package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.ixsi.dto.query.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.ChangedProvidersResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.CloseSessionResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.OpenSessionResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.PlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.TokenGenerationResponseDTO;
import de.rwth.idsg.bikeman.ixsi.schema.GeoCircleType;
import de.rwth.idsg.bikeman.ixsi.schema.GeoRectangleType;
import de.rwth.idsg.bikeman.ixsi.schema.ProviderPlaceIDType;

import java.util.List;

/**
 * Created by max on 06/10/14.
 */
public interface QueryIXSIRepository {

    // Static data
    BookingTargetsInfoResponseDTO bookingTargetInfos();
    ChangedProvidersResponseDTO changedProviders(long requestTimestamp);

    // User triggered data
    List<AvailabilityResponseDTO> availability(GeoCircleType circle);
    List<AvailabilityResponseDTO> availability(GeoRectangleType rectangle);
    // we do not need booking for velocity
//    BookingResponseDTObooking();
//    ChangeBookingResponseDTO changeBooking();
    CloseSessionResponseDTO closeSession();
    OpenSessionResponseDTO openSession();
    List<PlaceAvailabilityResponseDTO> placeAvailability(List<ProviderPlaceIDType> placeIds);
    List<PlaceAvailabilityResponseDTO> placeAvailability(GeoCircleType circle);
    List<PlaceAvailabilityResponseDTO> placeAvailability(GeoRectangleType geoRectangle);
    TokenGenerationResponseDTO tokenGeneration();
}
