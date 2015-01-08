package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.ixsi.dto.query.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.ChangedProvidersResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.PlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.schema.BookingTargetIDType;
import de.rwth.idsg.bikeman.ixsi.schema.GeoCircleType;
import de.rwth.idsg.bikeman.ixsi.schema.GeoRectangleType;

import java.util.List;

/**
 * Created by max on 06/10/14.
 */
public interface QueryIXSIRepository {

    // Static data
    BookingTargetsInfoResponseDTO bookingTargetInfos();
    ChangedProvidersResponseDTO changedProviders(long requestTimestamp);

    // User triggered data
    List<AvailabilityResponseDTO> availability(List<BookingTargetIDType> targets);
    List<AvailabilityResponseDTO> availability(GeoCircleType circle);
    List<AvailabilityResponseDTO> availability(GeoRectangleType rectangle);

    List<PlaceAvailabilityResponseDTO> placeAvailability(List<String> placeIdList);
    List<PlaceAvailabilityResponseDTO> placeAvailability(GeoCircleType circle);
    List<PlaceAvailabilityResponseDTO> placeAvailability(GeoRectangleType geoRectangle);

}
