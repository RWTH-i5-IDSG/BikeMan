package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.ixsi.dto.subscription.*;

/**
 * Created by max on 06/10/14.
 */
public interface SubscriptionIXSIRepository {
    // Subscr. Administration response
    public HeartBeatResponseDTO heartBeat();

    // Subscr. Response
    public AvailabilitySubscriptionResponseDTO availabilitySubscription();
    public AvailabilitySubscriptionStatusResponseDTO availabilitySubscriptionStatus();
    public PlaceAvailabilitySubscriptionResponseDTO placeAvailabilitySubscription();
    public PlaceAvailabilitySubscriptionStatusResponseDTO placeAvailabilitySubscriptionStatus();
    public BookingAlertSubscriptionResponseDTO bookingAlertSubscription();
    public BookingAlertSubscriptionStatusResponseDTO bookingAlertSubscriptionStatus();

    // Response Message
    public CompleteAvailabilityResponseDTO completeAvailability();
    public CompletePlaceAvailabilityResponseDTO completePlaceAvailability();
    public CompleteBookingAlertResponseDTO completeBookingAlert();
}
