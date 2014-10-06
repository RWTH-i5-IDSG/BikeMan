package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.ixsi.dto.query.*;

/**
 * Created by max on 06/10/14.
 */
public interface QueryIXSIRepository {

    // Static data
    public BookingTargetsInfoResponseDTO bookingTargetInfos();
    public ChangedProvidersResponseDTO changedProviders();

    // User triggered data
    public AvailabilityResponseDTO availability();
    public BookingResponseDTO booking();
    public ChangeBookingResponseDTO changeBooking();
    public CloseSessionResponseDTO closeSession();
    public OpenSessionResponseDTO openSession();
    public PlaceAvailabilityResponseDTO placeAvailability();
    public TokenGenerationResponseDTO tokenGeneration();
}
