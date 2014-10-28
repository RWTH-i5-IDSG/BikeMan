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
    public BookingTargetsInfoResponseDTO bookingTargetInfos();
    public ChangedProvidersResponseDTO changedProviders(long requestTimestamp);

    // User triggered data
    public List<AvailabilityResponseDTO> availability(GeoCircleType circle);
    public List<AvailabilityResponseDTO> availability(GeoRectangleType rectangle);
    // we do not need booking for velocity
//    public BookingResponseDTObooking();
//    public ChangeBookingResponseDTO changeBooking();
    public CloseSessionResponseDTO closeSession();
    public OpenSessionResponseDTO openSession();
    public List<PlaceAvailabilityResponseDTO> placeAvailability(List<ProviderPlaceIDType> placeIds);
    public List<PlaceAvailabilityResponseDTO> placeAvailability(GeoCircleType circle);
    public List<PlaceAvailabilityResponseDTO> placeAvailability(GeoRectangleType geoRectangle);
    public TokenGenerationResponseDTO tokenGeneration();
}
