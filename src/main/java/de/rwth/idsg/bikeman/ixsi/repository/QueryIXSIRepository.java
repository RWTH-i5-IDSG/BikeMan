package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.ixsi.dto.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.ChangedProvidersResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.PlaceAvailabilityResponseDTO;
import xjc.schema.ixsi.BookingTargetIDType;
import xjc.schema.ixsi.GeoCircleType;
import xjc.schema.ixsi.GeoRectangleType;

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
