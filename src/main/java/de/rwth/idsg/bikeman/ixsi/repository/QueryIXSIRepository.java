package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.ixsi.dto.query.AvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.ChangedProvidersResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.CloseSessionResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.OpenSessionResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.PlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.TokenGenerationResponseDTO;

/**
 * Created by max on 06/10/14.
 */
public interface QueryIXSIRepository {

    // Static data
    public BookingTargetsInfoResponseDTO bookingTargetInfos();
    public ChangedProvidersResponseDTO changedProviders(long requestTimestamp);

    // User triggered data
    public AvailabilityResponseDTO availability();
    // we do not need booking for velocity
//    public BookingResponseDTObooking();
//    public ChangeBookingResponseDTO changeBooking();
    public CloseSessionResponseDTO closeSession();
    public OpenSessionResponseDTO openSession();
    public PlaceAvailabilityResponseDTO placeAvailability();
    public TokenGenerationResponseDTO tokenGeneration();
}
